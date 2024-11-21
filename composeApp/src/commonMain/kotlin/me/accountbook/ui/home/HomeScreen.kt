package me.accountbook.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import me.accountbook.database.Account
import me.accountbook.database.Tagbox
import me.accountbook.platform.Platform
import me.accountbook.platform.getHomeLazyVerticalStaggeredGridColumns
import me.accountbook.platform.getPlatform
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.common.components.AccountCard
import me.accountbook.ui.common.components.BasicPage
import me.accountbook.ui.common.components.FunCard
import me.accountbook.ui.common.components.HorizontalScrollWithBar
import me.accountbook.ui.common.components.TagFlowRow
import me.accountbook.ui.home.viewmodel.HomeScreenViewModel
import me.accountbook.ui.navigation.Screen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(isLandscape: Boolean, navController: NavHostController) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    LaunchedEffect(Unit){
        viewModel.loadAccount()
        viewModel.loadSortedTagbox()
    }


    BasicPage(isLandscape, title = "首页") {

        // 使用 LazyVerticalGrid 创建自适应列
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(getHomeLazyVerticalStaggeredGridColumns()),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
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
                            viewModel.accounts.forEach { (id, account_name, balance, created_at) ->
                                AccountCard(title = account_name, balance = balance)
                            }
                        }
                    }
                )
            }

            item {
                FunCard(title = "标签",
                    icon = Icons.AutoMirrored.Outlined.Label,
                    content = {
                        TagFlowRow(viewModel.tagbox)
                    },
                    onClick = {
                        navController.navigate(Screen.TagDetails.route) {
                            // 防止重复添加相同的屏幕
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


fun isDesktop(): Boolean {
    return getPlatform() == Platform.Desktop
}
