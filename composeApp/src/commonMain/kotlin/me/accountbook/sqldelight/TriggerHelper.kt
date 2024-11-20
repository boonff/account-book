package me.accountbook.sqldelight

import app.cash.sqldelight.db.SqlDriver

open class TriggerHelper(private val driver: SqlDriver) {

    private fun createSetSortOrderTrigger() {
        val createTriggerSQL = """
            CREATE TRIGGER IF NOT EXISTS SetSortOrder
            AFTER INSERT ON Tagbox
            FOR EACH ROW
            BEGIN
                UPDATE Tagbox
                SET position = NEW.tagbox_id
                WHERE tagbox_id = NEW.tagbox_id;
            END;
        """.trimIndent()

        driver.execute(null, createTriggerSQL, 0)
    }

    fun initializeTriggers() {
        createSetSortOrderTrigger()
    }
}
