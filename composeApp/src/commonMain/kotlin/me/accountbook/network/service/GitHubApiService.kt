package me.accountbook.network.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.accountbook.utils.LoggingUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object GitHubApiService {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // 日志记录
        .build()


    //获取用户信息
    suspend fun fetchUserName(token: String): String? {
        @Serializable
        data class GitHubUser(
            val login: String
        )

        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization", "Bearer $token")
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val json = Json { ignoreUnknownKeys = true } // 序列化时忽略未知字段
                        return@withContext json.decodeFromString<GitHubUser>(it).login
                    }
                } else {
                    LoggingUtil.logError(
                        "用户名获取失败", Exception(
                            """
                            code: ${response.code}
                            body: ${response.body?.string()}
                        """.trimIndent()
                        )
                    )
                }
            } catch (e: Exception) {
                LoggingUtil.logError("用户名获取失败", e)
            }
            return@withContext null
        }
    }

    // 检查仓库是否已经存在
    suspend fun checkIfRepoExists(token: String, username: String, repoName: String): Boolean {
        val checkRepoUrl =
            "https://api.github.com/repos/$username/$repoName" // 替换为你的 GitHub 用户名
        val checkRepoRequest =
            Request.Builder().url(checkRepoUrl).addHeader("Authorization", "Bearer $token")
                .build()

        return withContext(Dispatchers.IO) {
            try {
                val checkRepoResponse: Response = client.newCall(checkRepoRequest).execute()
                checkRepoResponse.isSuccessful // 如果响应成功，表示仓库已经存在
            } catch (e: Exception) {
                false // 如果请求失败，表示仓库不存在
            }
        }
    }

    // 创建私有仓库
    suspend fun createPrivateRepo(
        token: String,
        username: String,
        repoName: String,
        description: String = "Private repository"
    ): Boolean {

        // 使用协程中的 withContext 切换到IO线程
        return withContext(Dispatchers.IO) {
            if (checkIfRepoExists(token, username, repoName)) {
                LoggingUtil.logInfo("Repository '$repoName' 已存在")
                return@withContext true
            }

            val url = "https://api.github.com/user/repos"
            val jsonPayload = """
                {
                    "name": "$repoName",
                    "description": "$description",
                    "private": true
                }
            """

            val requestBody: RequestBody =
                jsonPayload.trimIndent().toRequestBody("application/json".toMediaType())

            val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token")
                .post(requestBody).build()

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return@withContext true
                } else {
                    LoggingUtil.logError(
                        "repository 创建失败", Exception(
                            """
                            code: ${response.code}
                            body: ${response.body?.string()}
                        """.trimIndent()
                        )
                    )
                }
            } catch (e: Exception) {
                LoggingUtil.logError("repository 创建失败", e)
            }
            return@withContext false
        }
    }

    // 直接上传 ProtoBuf 数据到指定路径
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun uploadProtoBufToRepo(
        token: String,
        username: String,
        repoName: String,
        repoPath: String,
        protoBufBytes: ByteArray,
        commitMessage: String = "Upload ProtoBuf file"
    ): Boolean {
        val encodedFile = Base64.encode(protoBufBytes)
        val sha: String? = getFileShaFromRepo(token, username, repoName, repoPath)
        // GitHub API URL: 上传文件
        val url =
            "https://api.github.com/repos/$username/$repoName/contents/$repoPath"

        // 创建请求体
        val jsonPayload = """
            {
                "message": "$commitMessage",
                "content": "$encodedFile",
                "sha": "$sha"
            }
        """

        val requestBody: RequestBody =
            jsonPayload.trimIndent().toRequestBody("application/json".toMediaType())

        val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token")
            .put(requestBody) // 使用 PUT 请求上传文件
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return@withContext true
                } else {
                    LoggingUtil.logError(
                        "上传失败", Exception(
                            """
                            code: ${response.code}
                            body: ${response.body?.string()}
                        """.trimIndent()
                        )
                    )
                }
            } catch (e: Exception) {
                LoggingUtil.logError("上传失败", e)
            }
            return@withContext false
        }
    }

    //获取仓库文件的sha校验值
    private suspend fun getFileShaFromRepo(
        token: String,
        username: String,
        repoName: String,
        repoPath: String
    ): String? {
        val url =
            "https://api.github.com/repos/$username/$repoName/contents/$repoPath"

        val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token")
            .get()  // GET 请求获取文件的元数据
            .build()

        return withContext(Dispatchers.IO) {
            @Serializable
            data class FileInfoResponse(val sha: String)

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    val sha = jsonResponse?.let {
                        val json = Json { ignoreUnknownKeys = true }
                        json.decodeFromString<FileInfoResponse>(it).sha
                    }
                    return@withContext sha
                } else {
                    LoggingUtil.logError(
                        "sha校验值获取失败", Exception(
                            """
                            code: ${response.code}
                            body: ${response.body?.string()}
                        """.trimIndent()
                        )
                    )
                }
            } catch (e: Exception) {
                LoggingUtil.logError("sha校验值获取失败", e)
            }
            return@withContext null
        }
    }

    //获取仓库 ProtoBuf 数据
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun fetchFileContentAsByteArray(
        token: String,
        username: String,
        repoName: String,
        repoPath: String
    ): ByteArray? {

        val url =
            "https://api.github.com/repos/$username/$repoName/contents/$repoPath"

        val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token")
            .get()  // GET 请求获取文件内容
            .build()

        return withContext(Dispatchers.IO) {
            @Serializable
            data class FileContentResponse(
                val content: String, // Base64 编码的文件内容
                val encoding: String // 内容的编码方式
            )

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    jsonResponse?.let {
                        val json = Json { ignoreUnknownKeys = true }
                        val fileContentResponse = json.decodeFromString<FileContentResponse>(it)

                        if (fileContentResponse.encoding == "base64") {
                            // 解码 Base64 内容为 ByteArray
                            val cleanedContent =
                                fileContentResponse.content.replace("\n", "").replace("\r", "")
                            return@withContext Base64.decode(cleanedContent)
                        } else {
                            LoggingUtil.logDebug("不支持的编码: ${fileContentResponse.encoding}")
                        }
                    }
                } else {
                    LoggingUtil.logError(
                        "下传失败", Exception(
                            """
                            code: ${response.code}
                            body: ${response.body?.string()}
                        """.trimIndent()
                        )
                    )
                }
            } catch (e: Exception) {
                LoggingUtil.logError("下传失败", e)
            }
            return@withContext null
        }
    }


}

