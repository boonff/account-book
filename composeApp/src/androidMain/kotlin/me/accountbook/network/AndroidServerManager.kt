package me.accountbook.network

import org.koin.core.component.KoinComponent
class AndroidServerManager:KoinComponent {

    // 启动服务器的逻辑
    fun startServer(port: Int, rootDirectory: String) {
        embeddedServer
        println("服务器已启动，监听端口: $port，根目录: $rootDirectory")

    }

    // 停止服务器的逻辑
    fun stopServer() {
        // 在这里实现停止服务器的逻辑
        println("服务器已停止")
    }
}
