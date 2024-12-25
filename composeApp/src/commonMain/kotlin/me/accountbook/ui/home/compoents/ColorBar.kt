package me.accountbook.ui.home.compoents

// ui/home/Color.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object ColorPalette {
    val colors = listOf(
        Color(0xFFFF0000), // 红色
        Color(0xFFFF7F00), // 橙色
        Color(0xFFFFD700), // 黄色
        Color(0xFF008000), // 绿色
        Color(0xFF0000FF), // 蓝色
        Color(0xFF4B0082), // 靛蓝
        Color(0xFFEE82EE), // 紫色
        Color(0xFFFF1493)  // 深粉色
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorBar(
    modifier: Modifier = Modifier,
    onClick: (color: Color) -> Unit
) {

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (color in ColorPalette.colors) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .background(color, RoundedCornerShape(8.dp))
                    .clickable {
                        onClick(color)
                    }
            )
        }
    }
}