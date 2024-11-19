package me.accountbook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import me.accountbook.database.Tagbox

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagFlowRow(tags: List<Tagbox>) {
    FlowRow(
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach {  it ->
            TagCard(it.name, Color(it.color))
        }
    }
}

@Composable
fun TagCard(name: String, color: Color, modifier: Modifier = Modifier) {
    // 计算背景颜色的亮度，决定文字颜色
    val textColor = if (color.luminance() > 0) Color.Black else Color.White
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color) // 使用 CardDefaults 设置背景色
    ) {
        Text(
            text = name,
            color = textColor,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
