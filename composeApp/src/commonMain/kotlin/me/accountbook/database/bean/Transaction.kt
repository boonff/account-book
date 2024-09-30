package me.accountbook.database.bean

import java.text.SimpleDateFormat
import java.util.Date

data class Transaction(
    var id: Int,
    var tag: Tag,
    var save: Double,
    var date: Date
) {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
    }
}
