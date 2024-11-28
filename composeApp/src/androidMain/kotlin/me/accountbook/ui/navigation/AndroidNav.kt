package me.accountbook.ui.navigation

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.accountbook.utils.DeviceUtils


@Composable
fun AndroidNav() {
    val navHostController = rememberNavController()
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    var bottomBarVisible by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(currentBackStackEntry) {
        bottomBarVisible = currentBackStackEntry?.destination?.route in listOf(
            Screen.HomeScreen.route,
            Screen.TranslationScreen.route,
            Screen.SettingScreen.route
        )
    }

    if (DeviceUtils.isLandscape()) {
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

