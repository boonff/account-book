package me.accountbook.network.utils

import io.ktor.http.ContentType
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CompletableDeferred
import me.accountbook.koin.OAuthConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ServerUtil : KoinComponent {
    private val oAuthConfig: OAuthConfig by inject()

    // 持有服务器实例
    private var server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

    // 用于异步接收授权码
    private var authorizationCodeDeferred = CompletableDeferred<String?>()

    // 启动服务器（静默模式）
    fun startServer() {
        if (server != null) {
            return // 如果服务器已启动，则不重复启动
        }

        server = embeddedServer(CIO, oAuthConfig.port) {
            routing {
                // 静默处理获取 OAuth2 授权码
                get(oAuthConfig.rootDirectory) {
                    val code = call.parameters["code"]
                    if (code != null) {
                        authorizationCodeDeferred.complete(code) // 异步传递授权码
                        call.respondText("获取授权码成功，正在交换令牌，请稍等...")
                    } else {
                        call.respondText(
                            "授权码获取失败。",
                            ContentType.Text.Plain
                        )
                    }
                }
            }
        }.start(wait = false)
    }

    // 停止服务器
    fun stopServer() {
        server?.stop(1000, 2000)
        server = null
    }

    // 获取授权码（异步等待）
    suspend fun getAuthorizationCode(): String? {
        // 每次请求时确保重新创建新的 CompletableDeferred 实例
        authorizationCodeDeferred = CompletableDeferred()
        return try {
            val authorizationCode = authorizationCodeDeferred.await()
            if (authorizationCode.isNullOrBlank()) {
                println("Authorization code is null or empty")
            }
            authorizationCode
        } catch (e: Exception) {
            println("Error while waiting for authorization code: ${e.message}")
            null
        }
    }
}
