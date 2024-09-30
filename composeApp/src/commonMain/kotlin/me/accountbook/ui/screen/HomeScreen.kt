package me.accountbook.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import me.accountbook.Platform
import me.accountbook.data.TestData.cardDataList
import me.accountbook.getHomeLazyVerticalStaggeredGridColumns
import me.accountbook.getPlatform
import me.accountbook.ui.components.BarCard

import me.accountbook.ui.components.FunCart
import me.accountbook.ui.components.InfoRow
import me.accountbook.ui.components.ScrollProgressScreen

@Composable
fun HomeScreen(isLandscape: Boolean) {
    val gridCellsAdaptive = if (isDesktop()) 256 else 256
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background) // 使用主题背景色
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(), // 让列填满整个可用空间
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (isLandscape) {
                title("首页")
            }

            // 使用 LazyVerticalGrid 创建自适应列
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(getHomeLazyVerticalStaggeredGridColumns()),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                // FunCart 内容
                item {
                    FunCart("银行卡") {
                        HorizontalScrollExample {
                            cardDataList.forEach { (title, save) ->
                                BarCard(title = title, save = save) // 将 BarCard 作为参数传递
                            }
                        }
                    }
                }
                item {
                    FunCart("明细") {
                        InfoRow(color = Color.Red, title = "sss", subtitle = "sss", save = 100.0f)
                    }
                }
            }
        }
    }
}

fun isDesktop(): Boolean {
    return getPlatform() == Platform.Desktop
}


@Composable
fun title(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(bottom = 8.dp, top = 16.dp, start = 16.dp)
    )
}

@Composable
fun HorizontalScrollExample(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()


    ScrollProgressScreen() {
        // 包裹 Row 的 Box 用于计算宽度
        content()

    }
}


