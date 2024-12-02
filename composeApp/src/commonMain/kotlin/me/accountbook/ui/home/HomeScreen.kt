package me.accountbook.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.accountbook.platform.getHomeLazyVerticalStaggeredGridColumns
import me.accountbook.ui.common.components.BasicPage
import me.accountbook.ui.common.components.FunCard
import me.accountbook.ui.common.components.HorizontalScrollWithBar
import me.accountbook.ui.home.viewmodel.TagboxDetailsViewModel
import me.accountbook.ui.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavHostController) {
    val tagboxDetailsViewModel: TagboxDetailsViewModel = koinViewModel()

    //加载数据
    LaunchedEffect(Unit) {
        tagboxDetailsViewModel.loadSortedTagbox()
    }
    BasicPage(
        title = "首页",
        reLoadData = {
            tagboxDetailsViewModel.loadSortedTagbox()
        },
        content = {
            // 使用 LazyVerticalGrid 创建自适应列
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(getHomeLazyVerticalStaggeredGridColumns()),
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                // FunCart 内容
                item {
                    FunCard(
                        title = "钱包",
                        icon = Icons.Outlined.AccountBalanceWallet,
                        content = {
                            HorizontalScrollWithBar() {


                            }
                        }
                    )
                }
                item {
                    FunCard(title = "标签",
                        icon = Icons.AutoMirrored.Outlined.Label,
                        content = {
                            FlowRowTagbox(tagboxDetailsViewModel.tagboxList)
                        },
                        onClick = {
                            navController.navigate(Screen.TagDetails.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun AccountCard(
    title: String = "Anonymous",
    balance: Double,
    modifier: Modifier = Modifier // 添加 modifier 参数
) {
    Card(
        modifier = modifier
            .fillMaxSize(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom=4.dp)
            )

            //CompactInfoRow(color = TestData.SaveColor, balance = balance)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "银行描述",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
