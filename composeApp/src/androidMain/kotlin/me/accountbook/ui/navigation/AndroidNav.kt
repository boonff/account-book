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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import me.accountbook.ui.screen.HomeScreen
import me.accountbook.ui.screen.SettingsScreen
import me.accountbook.ui.screen.TranslationScreen
import me.accountbook.utils.DeviceUtils

@Composable
fun AndroidNav() {
    //获取屏幕尺寸信息
    rememberNavController()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    var isLandscape = DeviceUtils.isTablet(screenWidth, screenHeight) || DeviceUtils.isPortrait(screenWidth, screenHeight)

    var
    if (isLandscape) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .height(64.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    navItems.forEach { navItem ->
                        NavigationBarItem(
                            selected = selectedScreen == navItem.screen,
                            onClick = { selectedScreen = navItem.screen },
                            icon = { Icon(imageVector = navItem.iconImageVector, contentDescription = null)},
                            label = navItem.label
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)) {
                when (selectedScreen) {
                    is Screen.HomeScreen -> HomeScreen(isLandscape, selectedScreen)
                    is Screen.TranslationScreen -> TranslationScreen(isLandscape)
                    is Screen.SettingScreen -> SettingsScreen()
                    Screen.TagDetails -> TODO()
                }
            }
        }

    } else {
       Navigator(selectedScreen)
    }
}

