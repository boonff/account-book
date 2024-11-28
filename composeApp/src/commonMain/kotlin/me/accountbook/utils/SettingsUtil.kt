package me.accountbook.utils


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.accountbook.utils.file.FileUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

object SettingsUtil : KoinComponent {
    private val fileUtil: FileUtil by inject()
    private const val SETTINGS_FILE_PATH = "app_settings.json"


    // 保存设置到文件
    fun saveSettings(settings: AppSettings) {
        try {
            // 将设置对象序列化为 JSON 字符串
            val jsonString = Json.encodeToString(settings)
            // 将 JSON 写入文件
            File(SETTINGS_FILE_PATH).writeText(jsonString)
            println("Settings saved successfully.")
        } catch (e: Exception) {
            println("Error saving settings: ${e.message}")
        }
    }

    // 从文件加载设置
    fun loadSettings(): AppSettings? {
        return try {
            // 读取文件内容并反序列化为 AppSettings 对象
            val jsonString = File(SETTINGS_FILE_PATH).readText()
            Json.decodeFromString<AppSettings>(jsonString)
        } catch (e: Exception) {
            println("Error loading settings: ${e.message}")
            null
        }
    }

    // 检查设置文件是否存在
    fun settingsFileExists(): Boolean {
        return File(SETTINGS_FILE_PATH).exists()
    }
}


@Serializable
data class AppSettings(
    val theme: String,
)
