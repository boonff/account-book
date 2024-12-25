package me.accountbook.ui.home.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.accountbook.platform.getHomeLazyVerticalStaggeredGridColumns
import me.accountbook.ui.common.chart.StackedDonutChart
import me.accountbook.ui.common.components.BasicScreen
import me.accountbook.ui.common.components.FunCard
import me.accountbook.ui.common.components.HorizontalScrollWithBar
import me.accountbook.ui.common.components.SyncPoint
import me.accountbook.ui.common.components.viewmodel.SyncPointViewModel
import me.accountbook.ui.home.compoents.FlowRowTagbox
import me.accountbook.ui.home.viewmodel.HomeScreenViewModel
import me.accountbook.ui.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavHostController) {
    val homeScreenViewModel: HomeScreenViewModel = koinViewModel()
    val syncPointViewModel = SyncPointViewModel(homeScreenViewModel.tagboxStateManager)

    val tagboxList by homeScreenViewModel.tagboxList.collectAsState()
    BasicScreen(
        title = "首页",
        syncPoint = { },
    ) {
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
                    syncPoint = { Text("😀")},

                    onClick = {
                        navController.navigate(Screen.TagDetails.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ) {
                    HorizontalScrollWithBar() {
                        AccountMiniCard("test", 100.0)

                    }

                }
            }
            item {
                FunCard(title = "标签",
                    icon = Icons.AutoMirrored.Outlined.Label,
                    syncPoint = {
                        SyncPoint(
                            syncPointViewModel
                        ){
                            homeScreenViewModel.syncTagbox()
                        }
                    },
                    onClick = {
                        navController.navigate(Screen.TagDetails.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ){
                    FlowRowTagbox(tagboxList)
                }
            }
        }
    }
}

@Composable
fun AccountMiniCard(
    title: String = "Anonymous",
    balance: Double,
    modifier: Modifier = Modifier // 添加 modifier 参数
) {
    Surface(
        modifier = Modifier
            .padding(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            StackedDonutChart(
                listOf(0.2f, 0.3f),
                listOf(Color.Red.copy(0.8f), Color.Green.copy(0.8f)),
                modifier.size(32.dp)
            )
        }
    }
}