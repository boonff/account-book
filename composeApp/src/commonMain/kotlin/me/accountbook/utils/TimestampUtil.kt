package me.accountbook.utils

import java.time.Instant

object TimestampUtil {
    fun getTimestamp():Long{
        return Instant.now().epochSecond
    }
}