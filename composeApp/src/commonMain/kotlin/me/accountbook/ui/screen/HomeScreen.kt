package me.accountbook.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.accountbook.platform.Platform
import me.accountbook.data.TestData.CardDataList
import me.accountbook.data.TestData.TagList
import me.accountbook.platform.getHomeLazyVerticalStaggeredGridColumns
import me.accountbook.platform.getPlatform
import me.accountbook.ui.components.BarCard

import me.accountbook.ui.components.FunCart
import me.accountbook.ui.components.HorizontalScrollWithBar
import me.accountbook.ui.components.ReorderTag
import me.accountbook.ui.components.TagFlowRow

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
                        HorizontalScrollWithBar() {
                            CardDataList.forEach { (title, save) ->
                                BarCard(title = title, save = save)
                            }
                        }

                    }
                }

                item {
                    FunCart("明细") {
                        FunCart("标签") {
                            Box(
                                modifier = Modifier
                                    .height(500.dp)
                                    .fillMaxWidth()
                            )
                            {
                                ReorderTag(TagList.map{it.name})
                            }

                        }
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