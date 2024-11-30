package me.accountbook.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import me.accountbook.utils.serialization.SerializableDatabase
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
    private const val repoName: String = "test"
    private const val repoPath: String = "database.bat"
    private var token: String? = null
    var username: String? = null

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // 日志记录
        .build()

    private fun emptyToken(): Boolean {
        return token == null
    }

    private fun emptyUsername(): Boolean {
        return username == null
    }

    fun setToken(token: String?) {
        this.token = token
    }

    suspend fun loadUsername(): Boolean {
        username = fetchUserName()
        return username != null
    }

    //获取用户信息
    suspend fun fetchUserName(): String? {
        if (emptyToken()) return null
        @Serializable
        data class GitHubUser(
            val login: String
        )

        // 使用协程中的 withContext 切换到IO线程
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
                    // 处理错误响应
                    println("Error: ${response.code}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
            return@withContext null
        }
    }

    // 检查仓库是否已经存在
    private suspend fun checkIfRepoExists(): Boolean {
        if (emptyToken() || emptyUsername()) return false
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
    suspend fun createPrivateRepo(description: String = "Private repository"): Boolean {
        if (emptyToken()) return false

        // 使用协程中的 withContext 切换到IO线程
        return withContext(Dispatchers.IO) {
            if (checkIfRepoExists()) {
                println("Repository '$repoName' already exists.")
                return@withContext false
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
                    println("Repository '$repoName' created successfully.")
                    return@withContext true
                } else {
                    println("Failed to create repository: ${response.code}")
                    val errorBody = response.body?.string()
                    println("Error: $errorBody")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
            return@withContext false
        }
    }

    // 直接上传 ProtoBuf 数据到指定路径
    @OptIn(ExperimentalSerializationApi::class, ExperimentalEncodingApi::class)
    suspend fun uploadProtoBufToRepo(
        protoBufData: SerializableDatabase,
        commitMessage: String = "Upload ProtoBuf file"
    ): Boolean {
        if (emptyToken() || emptyUsername()) return false
        val protoBufBytes = ProtoBuf.encodeToByteArray(protoBufData)
        val encodedFile = Base64.encode(protoBufBytes)
        val sha: String? = getFileShaFromRepo()
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
                    println("File uploaded successfully.")
                    return@withContext true
                } else {
                    println("Failed to upload file: ${response.code}")
                    val errorBody = response.body?.string()
                    println("Error: $errorBody")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
            return@withContext false
        }
    }

    //获取仓库文件的sha校验值
    private suspend fun getFileShaFromRepo(): String? {
        if (emptyToken() || emptyUsername()) return null
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
                    println("Failed to fetch file info: ${response.code}")
                    val errorBody = response.body?.string()
                    println("Error: $errorBody")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
            return@withContext null
        }
    }

    //获取仓库 ProtoBuf 数据
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun fetchFileContentAsByteArray(): ByteArray? {
        if (emptyToken() || emptyUsername()) return null

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
                            println("Unsupported encoding: ${fileContentResponse.encoding}")
                        }
                    }
                } else {
                    println("Failed to fetch file content: ${response.code}")
                    val errorBody = response.body?.string()
                    println("Error: $errorBody")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
            return@withContext null
        }
    }

}

