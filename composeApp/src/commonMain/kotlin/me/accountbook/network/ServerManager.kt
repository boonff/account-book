package me.accountbook.network

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.application.call
import kotlinx.coroutines.CompletableDeferred
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ServerManager() : KoinComponent {
    private val port: Int by inject(named("port"))
    private val rootDirectory: String by inject(named("rootDirectory"))

    // 持有服务器实例
    private var server: ApplicationEngine? = null

    // 用于异步接收授权码
    private val authorizationCodeDeferred = CompletableDeferred<String?>()

    // 启动服务器（静默模式）
    fun startServer() {
        if (server != null) {
            return // 如果服务器已启动，则不重复启动
        }

        server = embeddedServer(Netty, port) {
            routing {
                // 静默处理获取 OAuth2 授权码
                get("/$rootDirectory") {
                    val code = call.parameters["code"]
                    if (code != null) {
                        authorizationCodeDeferred.complete(code) // 异步传递授权码
                        call.respondText("Authorization successful. You can close this page now.")
                    } else {
                        call.respondText(
                            "Authorization failed. No code received.",
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
    suspend fun getAuthorizationCode(): String {
        return try {
            val authorizationCode = authorizationCodeDeferred.await()
            if (authorizationCode.isNullOrBlank()) {
                throw IllegalStateException("Authorization code is null or empty")
            }
            authorizationCode
        } catch (e: Exception) {
            println("Error while waiting for authorization code: ${e.message}")
            throw e
        }
    }
}
