package me.accountbook.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun ScrollProgressScreen(
    content: @Composable () -> Unit
) {
    var viewportWidth by remember { mutableStateOf(0) }
    var contentWidth by remember { mutableStateOf(0) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrollBarOffset by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 可滚动内容
        Box(
            modifier = Modifier
                .onSizeChanged {
                    viewportWidth = it.width // 更新视口宽度
                }
                .horizontalScroll(scrollState) // 添加横向滚动
        ) {
            Row(
                modifier = Modifier.onSizeChanged {
                    contentWidth = it.width // 更新视口宽度
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content() // 渲染内容
            }
        }

        val isContentReady = viewportWidth > 0 && contentWidth > 0
        if (isContentReady) {
            val scrollBarWidth = (viewportWidth.toFloat() / contentWidth) * viewportWidth

            Box(
                modifier = Modifier
                    .width(pxToDp(scrollBarWidth))
                    .height(8.dp)
                    .offset(x = pxToDp(scrollBarOffset))
                    .background(Color.Gray)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            scrollBarOffset += dragAmount
                            // 在协程中更新 scrollState
                            coroutineScope.launch {
                                val maxScroll = scrollState.maxValue
                                val scrollDelta = (scrollBarOffset / size.width) * maxScroll
                                scrollState.scrollTo(scrollState.value + scrollDelta.toInt())
                            }
                        }
                    }

            )
            Text(text = "contentWidth: $contentWidth")
            Text(text = "viewportWidth: $viewportWidth")
            Text(text = "scrollState.maxValue: ${scrollState.maxValue}")
        }
    }
}

@Composable
fun pxToDp(px: Float): Dp {
    val density = LocalDensity.current
    return with(density) { px.toDp() }
}
