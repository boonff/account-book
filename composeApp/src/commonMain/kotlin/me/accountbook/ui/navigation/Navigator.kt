package me.accountbook.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import me.accountbook.ui.screen.HomeScreen
import me.accountbook.ui.screen.SettingsScreen
import me.accountbook.ui.screen.TranslationScreen
import me.accountbook.ui.screen.isDesktop


sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object TranslationScreen : Screen("transaction")
    object SettingScreen : Screen("settings")
    object TagDetails : Screen("tagDetails")
}

@Composable
fun Navigator(navController: NavHostController) {
    Row(modifier = Modifier.fillMaxSize()) {
        // 导航栏
        NavigationRail(
            modifier = Modifier
                .width(64.dp),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            navItems.forEach { navItem ->
                NavigationRailItem(
                    selected = navController.currentBackStackEntry?.destination?.route == navItem.screen.route,
                    onClick = {
                        // 使用 NavController 导航
                        navController.navigate(navItem.screen.route) {
                            // 防止重复添加相同的屏幕
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

        // 在导航栏旁边放置内容区域
        Box(modifier = Modifier.fillMaxSize()) {
            // 使用 NavHost 来显示屏幕
            NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
                composable(Screen.HomeScreen.route) {
                    HomeScreen(isDesktop())
                }
                composable(Screen.TranslationScreen.route) {
                    TranslationScreen(isDesktop())
                }
                composable(Screen.SettingScreen.route) {
                    SettingsScreen()
                }
                composable(Screen.TagDetails.route) {
                    // TODO: 需要实现 TagDetailsScreen
                }
            }
        }
    }
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
    )
)
