package me.accountbook.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Label
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
import me.accountbook.ui.components.BasicPage

import me.accountbook.ui.components.FunCart
import me.accountbook.ui.components.HorizontalScrollWithBar
import me.accountbook.ui.components.ReorderTag
import me.accountbook.ui.components.TagFlowRow

@Composable
fun HomeScreen(isLandscape: Boolean) {
    val gridCellsAdaptive = if (isDesktop()) 256 else 256
    BasicPage(isLandscape, title = "首页") {

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
                FunCart(
                    title = "银行卡",
                    icon = Icons.Rounded.CreditCard,
                    content = {
                        HorizontalScrollWithBar() {
                            CardDataList.forEach { (title, save) ->
                                BarCard(title = title, save = save)
                            }
                        }
                    }
                )
            }

            item {
                FunCart(title = "标签",
                    icon = Icons.AutoMirrored.Rounded.Label,
                    content = {
                        ReorderTag(TagList)
                    },
                    onClick = {

                    }
                )
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

