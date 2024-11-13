package me.accountbook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import me.accountbook.database.bean.Tag
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import me.accountbook.data.TestData.TagList
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagFlowRow(tags: List<Tag>) {

    FlowRow(
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach {  it ->
            TagCard(it.name, it.color)
        }
    }
}

@Composable
fun TagCard(name: String, color: Color, modifier: Modifier = Modifier) {
    // 计算背景颜色的亮度，决定文字颜色
    val textColor = if (color.luminance() > 0.5) Color.Black else Color.White
    Card(
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = name,
            color = textColor,
            modifier = Modifier
                .background(color = color )
                .padding(8.dp)
        )
    }
}