package me.accountbook.data

import androidx.compose.ui.graphics.Color
import me.accountbook.database.bean.BankCard
import me.accountbook.database.bean.Tag
import java.math.BigDecimal

object TestData {
    val Save = 1000.0f
    val WarnSave = 600.0f
    val TargetSave = 800.0f

    val SaveColor = Color(0xFF04B97F)
    val TargetColor = Color(0xFFFFDC78)
    val WarnColor = Color(0xFFFF6951)
    val OverColor = Color(0xFF3CB0EE)
    val day_limit: Double = 1000.0

    // 标签列表
    val TagList = listOf(
        Tag("点外卖", Color(0xFF04B97F), 0),
        Tag("医疗", Color(0xFFFF6951), 1),
        Tag("steam", Color(0xFF3CB0EE),2),
        Tag("零食", Color(0xFFCE8F8F),3),
        Tag("交通", Color(0xFF6D6E71),4),
        Tag("住房基金", Color(0xFFBD8C8C),5),
        Tag("医疗保险", Color(0xFF8B6F8F),6),
        Tag("意外险", Color(0xFF8CC6F7),7),
        Tag("人寿险", Color(0xFFC4C6CF),8),
        Tag("健身", Color(0xFF7D8C9D),9)
    )

    //银行卡列表
    val CardDataList = listOf(
        "Card 1" to 50f,
        "Card 2" to 100f,
        "Card 3" to 150f,
        "Card 4" to 50f,
        "Card 5" to 100f,
        "Card 6" to 150f,
    )



}