package me.accountbook.data

import androidx.compose.ui.graphics.Color
import me.accountbook.database.bean.Tag

object TestData{
    val Save = 1000.0f
    val WarnSave =600.0f
    val TargetSave = 800.0f

    val SaveColor = Color(0xFF04B97F)
    val TargetColor = Color(0xFFFFDC78)
    val WarnColor = Color(0xFFFF6951)
    val OverColor = Color(0xFF3CB0EE)
    val day_limit: Double = 1000.0

    // 标签列表
    val Tags: List<Tag> = listOf(
        Tag("生活", Color(0xFF04B97F)),
        Tag("医疗", Color(0xFFFF6951)),
        Tag("娱乐", Color(0xFF3CB0EE)),
        Tag("餐饮", Color(0xFFCE8F8F)),
        Tag("交通", Color(0xFF6D6E71)),
        Tag("购物", Color(0xFFBD8C8C)),
        Tag("教育", Color(0xFF8B6F8F)),
        Tag("旅行", Color(0xFF8CC6F7)),
        Tag("保险", Color(0xFFC4C6CF)),
        Tag("健身", Color(0xFF7D8C9D))
    )

    //银行卡列表
    val cardDataList = listOf(
        "Card 1" to 50f,
        "Card 2" to 100f,
        "Card 3" to 150f,
        "Card 4" to 50f,
        "Card 5" to 100f,
        "Card 6" to 150f,
        "Card 1" to 50f,
        "Card 2" to 100f,
        "Card 3" to 150f,
        "Card 4" to 50f,
        "Card 5" to 100f,
        "Card 6" to 150f,
        "Card 1" to 50f,
        "Card 2" to 100f,
        "Card 3" to 150f,
        "Card 4" to 50f,
        "Card 5" to 100f,
        "Card 6" to 150f,
    )
}