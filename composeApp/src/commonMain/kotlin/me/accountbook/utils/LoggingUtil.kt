package me.accountbook.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import me.accountbook.file.local.FileUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object LoggingUtil : KoinComponent {
    private val logger = KotlinLogging.logger {}
    private const val LOG_FILE_NAME = "application.log"

    private val fileUtil: FileUtil by inject()

    init {
        // 创建日志文件
        if (!fileUtil.exist(LOG_FILE_NAME)) {
            fileUtil.save(LOG_FILE_NAME, "")
        }
    }

    fun logInfo(message: String) {
        val stackTraceInfo = getCallerInfo()
        logger.info { "$stackTraceInfo INFO: $message" }
        saveLogToFile("INFO: $stackTraceInfo $message")
    }

    fun logError(message: String, throwable: Throwable? = null) {
        val stackTraceInfo = getCallerInfo()
        logger.error(throwable) { "$stackTraceInfo ERROR: $message" }
        saveLogToFile("ERROR: $stackTraceInfo $message\n${throwable?.message}")
    }

    fun logDebug(message: String) {
        val stackTraceInfo = getCallerInfo()
        logger.debug { "$stackTraceInfo DEBUG: $message" }
        saveLogToFile("DEBUG: $stackTraceInfo $message")
    }

    private fun getCallerInfo(): String {
        // 获取调用者的堆栈信息
        val stackTrace = Throwable().stackTrace
        // 获取堆栈中的第一个有效调用信息
        val caller = stackTrace.getOrNull(3) // 获取调用者的栈帧
        return if (caller != null) {
            "at ${caller.className}.${caller.methodName}(${caller.fileName}:${caller.lineNumber})"
        } else {
            "Unknown caller"
        }
    }

    private fun saveLogToFile(logMessage: String) {
        try {
            // 添加时间戳和日志级别
            val timestampedMessage = "${java.time.LocalDateTime.now()} $logMessage"

            // 保存日志到文件
            val currentLog = fileUtil.read(LOG_FILE_NAME) ?: ""
            fileUtil.save(LOG_FILE_NAME, currentLog + "\n" + timestampedMessage)
        } catch (e: Exception) {
            logger.error { "Failed to save log to file: $e" }
        }
    }
}
