package me.accountbook

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform