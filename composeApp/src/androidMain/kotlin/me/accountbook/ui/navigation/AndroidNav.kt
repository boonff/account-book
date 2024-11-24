package me.accountbook.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.accountbook.ui.home.HomeScreen
import me.accountbook.ui.setting.SettingsScreen
import me.accountbook.ui.home.DetailsTagbox
import me.accountbook.ui.setting.sync.SyncDetails
import me.accountbook.ui.translation.TranslationScreen
import me.accountbook.utils.DeviceUtils

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedMutableState", "UnusedContentLambdaTargetStateParameter")
@Composable
fun AndroidNav() {
    val navHostController = rememberNavController()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = DeviceUtils.isTablet(screenWidth, screenHeight) || DeviceUtils.isPortrait(
        screenWidth,
        screenHeight
    )
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    var bottomBarVisible by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(currentBackStackEntry) {
        if (currentBackStackEntry?.destination?.route !in listOf(
                Screen.HomeScreen.route,
                Screen.TranslationScreen.route,
                Screen.SettingScreen.route
            )
        ) {
            bottomBarVisible = false
        } else {
            bottomBarVisible = true
        }
    }

    if (isLandscape) {
        Scaffold(
            bottomBar = {
                if (bottomBarVisible)
                    BottomAppBar(
                        modifier = Modifier.height(64.dp),
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        navItems.forEach { navItem ->
                            val selected =
                                currentBackStackEntry?.destination?.route == navItem.screen.route

                            NavigationBarItem(
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
                                        contentDescription = null
                                    )
                                },
                                label = navItem.label
                            )
                        }
                    }
            }
        ) { paddingValue ->
            MyNavController(
                navHostController,
                Screen.HomeScreen.route,
                modifier = Modifier.padding(paddingValue)
            )
        }
    } else {
        Navigator(navHostController)
    }
}

