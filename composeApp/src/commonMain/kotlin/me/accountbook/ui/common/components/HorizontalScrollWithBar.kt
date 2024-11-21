package me.accountbook.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun HorizontalScrollWithBar(
    content: @Composable () -> Unit
) {
    var viewportWidth by remember { mutableStateOf(0) }
    var contentWidth by remember { mutableStateOf(0) }
    var maxScrollBarOffset by remember { mutableStateOf(0.0f) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrollBarOffset by remember { mutableStateOf(0f) }
    LaunchedEffect(scrollState.value) {
        scrollBarOffset =
            (scrollState.value.toFloat() / scrollState.maxValue) * maxScrollBarOffset
        scrollBarOffset = scrollBarOffset.coerceIn(0f, maxScrollBarOffset) // 确保在可见范围内

    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        // 主体可滚动内容
        Box(
            modifier = Modifier
                .onSizeChanged {
                    viewportWidth = it.width //获取视图宽度
                }
                .onSizeChanged {
                    scrollBarOffset =
                        (scrollBarOffset - 1).coerceIn(0F, maxScrollBarOffset) //防止缩小窗口时滚动条消失
                }
                .horizontalScroll(scrollState) // 添加横向滚动
        ) {
            Row(
                modifier = Modifier.onSizeChanged {
                    contentWidth = it.width // 获取内容宽度
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        }

        //仅在获取到正常参数后渲染滚动条
        if (viewportWidth > 0 && contentWidth > 0) {
            val scrollBarWidth = (viewportWidth.toFloat() / contentWidth) * viewportWidth
            maxScrollBarOffset = viewportWidth - scrollBarWidth
            // 同步滚动条位置

            //滚动条
            Box(
                modifier = Modifier
                    .width(pxToDp(scrollBarWidth))
                    .padding(top = 8.dp)
                    .height(8.dp)
                    .offset(x = pxToDp(scrollBarOffset))
                    .background(Color.Gray, RoundedCornerShape(16.dp)) // 直接对背景应用圆角
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            // 根据拖拽的距离更新 scrollState
                            coroutineScope.launch {
                                scrollBarOffset =
                                    (scrollBarOffset + dragAmount).coerceIn(0f, maxScrollBarOffset)
                                scrollState.scrollTo((scrollBarOffset * ((contentWidth - viewportWidth) / maxScrollBarOffset)).toInt())
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun pxToDp(px: Float): Dp {
    val density = LocalDensity.current
    return with(density) { px.toDp() }
}


