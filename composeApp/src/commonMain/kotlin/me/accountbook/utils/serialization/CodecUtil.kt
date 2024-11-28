package me.accountbook.utils.serialization

import kotlinx.serialization.*
import me.accountbook.database.DatabaseHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

@OptIn(ExperimentalSerializationApi::class)
object CodecUtil : KoinComponent {
    private val dbHelper: DatabaseHelper by inject()

    fun serializationDatabase(): SerializableDatabase {
        val tagbox = dbHelper.queryAllTagbox()
        val serializableTagbox = tagbox.map {
            it.encode()
        }
        return SerializableDatabase(serializableTagbox)
    }
}
