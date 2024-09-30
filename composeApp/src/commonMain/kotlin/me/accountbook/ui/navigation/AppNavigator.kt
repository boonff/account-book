package me.accountbook.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.accountbook.ui.screen.HomeScreen
import me.accountbook.ui.screen.SettingsScreen
import me.accountbook.ui.screen.TransactionScreen
import me.accountbook.ui.screen.isDesktop

data class NavItem(
    val screen: Screen,
    val icon: @Composable () -> Unit,
    val label: @Composable () -> Unit
)

val navItems = listOf(
    NavItem(
        screen = Screen.HomeScreen,
        icon = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "首页",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        label = {
            Text(
                text = "首页",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    ),
    NavItem(
        screen = Screen.TranscationScreen,
        icon = {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = "交易",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        label = {
            Text(
                "交易", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    ),
    NavItem(
        screen = Screen.SettingScreen,
        icon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "设置",
                tint = MaterialTheme.colorScheme.onBackground

            )
        },
        label = {
            Text(
                "设置", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    )
)

@Composable
fun Navigator() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.HomeScreen) }
    var selectedScreen by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.HomeScreen) }

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier
                .width(64.dp),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            navItems.forEach { navItem ->
                NavigationRailItem(
                    selected = selectedScreen == navItem.screen,
                    onClick = { selectedScreen = navItem.screen },
                    icon = navItem.icon,
                    label = navItem.label
                )
            }
        }

        // 在 NavigationRail 旁边放置内容
        Box(
            modifier = Modifier
        ) {
            when (selectedScreen) {
                is Screen.HomeScreen -> HomeScreen(isDesktop())
                is Screen.TranscationScreen -> TransactionScreen(isDesktop())
                is Screen.SettingScreen -> SettingsScreen()
            }
        }
    }
}