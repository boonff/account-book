package me.accountbook.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.accountbook.ui.common.components.pxToDp

@Composable
fun ColorBar(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal
) {

    var colorBarWidth by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var thisColor by remember { mutableStateOf(Color.Red) }
    var colorPointOffset by remember { mutableStateOf(0f) }
    val colorPointSize = 100f
    val blockCount = colors.size // 色块的数量
    val gradient = if (orientation == Orientation.Horizontal) {
        Brush.horizontalGradient(colors)
    } else {
        Brush.verticalGradient(colors)
    }
    LaunchedEffect(colorPointOffset) {
        colorPointOffset =
            (colorPointOffset / colorBarWidth) * colorBarWidth
        colorPointOffset =
            colorPointOffset.coerceIn(0f, colorBarWidth.toFloat() - colorPointSize) // 确保在可见范围内
    }
    Column {
        Canvas(
            modifier = modifier
                .onSizeChanged {
                    colorBarWidth = it.width // 获取视图宽度
                }
                .fillMaxWidth()
                .height(16.dp)
        ) {
            val blockWidth = size.width / blockCount // 每个色块的宽度
            for (i in 0 until blockCount) {
                drawRect(
                    color = colors[i],
                    topLeft = androidx.compose.ui.geometry.Offset(x = i * blockWidth, y = 0f),
                    size = androidx.compose.ui.geometry.Size(
                        width = blockWidth,
                        height = size.height
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .height(pxToDp(colorPointSize))
                .width(pxToDp(colorPointSize))
                .offset(x = pxToDp(colorPointOffset))
                .background(thisColor) // 直接对背景应用圆角
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            colorPointOffset =
                                (colorPointOffset + dragAmount).coerceIn(
                                    0f,
                                    colorBarWidth.toFloat() - colorPointSize
                                )
                        }
                    }
                }
        )
    }


}
