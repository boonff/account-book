package me.accountbook.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import me.accountbook.sqldelight.DatabaseDriverFactory
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.screen.HomeScreen
import me.accountbook.ui.screen.SettingsScreen
import me.accountbook.ui.screen.TagDetails
import me.accountbook.ui.screen.TranslationScreen
import me.accountbook.utils.DeviceUtils
import org.koin.compose.koinInject

@Composable
fun AndroidNav(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = DeviceUtils.isTablet(screenWidth, screenHeight) || DeviceUtils.isPortrait(screenWidth, screenHeight)


    if (isLandscape) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(64.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    navItems.forEach { navItem ->
                        // 使用 currentBackStackEntryAsState 获取当前选中的导航项
                        val currentBackStackEntry by navController.currentBackStackEntryAsState()
                        val selected = currentBackStackEntry?.destination?.route == navItem.screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(navItem.screen.route) {
                                    // 防止重复添加相同的屏幕
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = navItem.iconImageVector, contentDescription = null) },
                            label = navItem.label
                        )
                    }
                }
            }
        ) { paddingValues ->
            // 内容区域，通过 NavController 管理的 NavHost 来显示不同的屏幕
            Box(modifier = Modifier.padding(paddingValues)) {
                NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
                    composable(Screen.HomeScreen.route) {
                        HomeScreen(isLandscape, navController)
                    }
                    composable(Screen.TranslationScreen.route) {
                        TranslationScreen(isLandscape)
                    }
                    composable(Screen.SettingScreen.route) {
                        SettingsScreen()
                    }
                    composable(Screen.TagDetails.route) {
                        TagDetails(navController)
                    }
                }
            }
        }
    } else {
        // 如果是竖屏或其他条件，直接使用Navigator（如果你有其他导航方式的话）
        Navigator(navController)
    }
}

