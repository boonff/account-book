package me.accountbook.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.material3.BottomAppBar
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
import me.accountbook.ui.screen.HomeScreen
import me.accountbook.ui.screen.SettingsScreen
import me.accountbook.ui.screen.TransactionScreen
import me.accountbook.utils.DeviceUtils





@Composable
fun AndroidNav() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    var selectedScreen by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.HomeScreen) }
    var isLandscape = DeviceUtils.isTablet(screenWidth, screenHeight) || DeviceUtils.isPortrait(screenWidth, screenHeight)
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
                            icon = navItem.icon,
                            label = navItem.label
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)) {
                when (selectedScreen) {
                    is Screen.HomeScreen -> HomeScreen(isLandscape)
                    is Screen.TranscationScreen -> TransactionScreen(isLandscape)
                    is Screen.SettingScreen -> SettingsScreen()
                }
            }
        }

    } else {
       Navigator()
    }
}


