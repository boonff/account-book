package me.accountbook.ui.screen


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
import androidx.navigation.NavHostController
import me.accountbook.platform.Platform
import me.accountbook.data.TestData.TagList
import me.accountbook.database.Account
import me.accountbook.platform.getHomeLazyVerticalStaggeredGridColumns
import me.accountbook.platform.getPlatform
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.components.AccountCard
import me.accountbook.ui.components.BasicPage
import me.accountbook.ui.components.FunCard
import me.accountbook.ui.components.HorizontalScrollWithBar
import me.accountbook.ui.components.TagFlowRow
import me.accountbook.ui.navigation.Screen
import org.koin.compose.koinInject

@Composable
fun HomeScreen(isLandscape: Boolean, navController: NavHostController) {
    val databaseHelper: DatabaseHelper = koinInject()
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    LaunchedEffect(Unit){
        accounts = databaseHelper.queryAllAccount()
        databaseHelper.close()
    }

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
                FunCard(
                    title = "钱包",
                    icon = Icons.Outlined.AccountBalanceWallet,
                    content = {
                        HorizontalScrollWithBar() {

                            accounts.forEach { (id, user_id, account_name, balance, created_at) ->
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
                        TagFlowRow(TagList)
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
