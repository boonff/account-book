package me.accountbook.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PanTool
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import me.accountbook.ui.home.screen.AccountDetails
import me.accountbook.ui.home.screen.HomeScreen
import me.accountbook.ui.setting.SettingsScreen
import me.accountbook.ui.home.screen.TagboxDetail
import me.accountbook.ui.translation.TranslationScreen
import me.accountbook.ui.setting.sync.SyncDetail

@Composable
fun Navigator(navHostController: NavHostController) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier
                .width(64.dp),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            navItems.forEach { navItem ->
                val selected = currentBackStackEntry?.destination?.route == navItem.screen.route

                NavigationRailItem(
                    selected = selected,
                    onClick = {
                        navHostController.navigate(navItem.screen.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = navItem.iconImageVector,
                            contentDescription = navItem.iconContentDescription,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = navItem.label
                )
            }
        }
        MyNavController(navHostController, Screen.HomeScreen.route)
    }
}

@Composable
fun MyNavController(
    navHostController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 使用 NavHost 来显示屏幕
        NavHost(navController = navHostController, startDestination = startDestination) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(navHostController)
            }
            composable(Screen.TranslationScreen.route) {
                TranslationScreen()
            }
            composable(Screen.SettingScreen.route) {
                SettingsScreen(navHostController)
            }
            composable(Screen.TagDetails.route) {
                TagboxDetail(navHostController)
            }
            composable(Screen.SyncDetails.route) {
                SyncDetail(navHostController)
            }
            composable(Screen.AccountDetails.route) {
                AccountDetails(navHostController)
            }
        }
    }
}

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object TranslationScreen : Screen("transaction")
    data object SettingScreen : Screen("settings")
    data object TagDetails : Screen("tagDetails")
    data object SyncDetails : Screen("syncDetails")
    data object AccountDetails : Screen("accountDetails")
}

// 定义每个导航项
data class NavItem(
    val screen: Screen,
    val iconImageVector: ImageVector,
    val iconContentDescription: String,
    val label: @Composable () -> Unit
)


// 定义导航项列表
val navItems = listOf(
    NavItem(
        screen = Screen.HomeScreen,
        iconImageVector = Icons.Outlined.Home,
        iconContentDescription = "首页",
        label = { }
    ),
    NavItem(
        screen = Screen.TranslationScreen,
        iconImageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
        iconContentDescription = "交易",
        label = { }
    ),
    NavItem(
        screen = Screen.SettingScreen,
        iconImageVector = Icons.Outlined.Settings,
        iconContentDescription = "设置",
        label = { }
    ),
)
