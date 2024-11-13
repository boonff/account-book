package me.accountbook.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.accountbook.ui.screen.HomeScreen
import me.accountbook.ui.screen.SettingsScreen
import me.accountbook.ui.screen.TransactionScreen
import me.accountbook.ui.screen.isDesktop

@Composable
fun Navigator() {
    var selectedScreen by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.HomeScreen) }

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier
                .width(64.dp),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            navItems.forEach { navItem ->
                NavigationRailItem(
                    selected = selectedScreen == navItem.screen,
                    onClick = { selectedScreen = navItem.screen },
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

data class NavItem(
    val screen: Screen,
    val iconImageVector: ImageVector,
    val iconContentDescription: String,
    val label: @Composable () -> Unit
)

val navItems = listOf(
    NavItem(
        screen = Screen.HomeScreen,
        iconImageVector = Icons.Outlined.Home,
        iconContentDescription = "首页",
        label = { }
    ),
    NavItem(
        screen = Screen.TranscationScreen,
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
