package me.accountbook.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.accountbook.ui.common.components.BasicPage
import me.accountbook.ui.navigation.Screen

@Composable
fun SettingsScreen(navHostController: NavHostController) {
    BasicPage(title = "设置") {
        Column {
            OptionsCard(
                name = "同步",
                Icons.Outlined.Sync,
                Screen.SyncDetails.route,
                navHostController = navHostController
            )
        }
    }
}

@Composable
fun OptionsCard(
    name: String,
    icon: ImageVector,
    route: String,
    navHostController: NavHostController
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navHostController.navigate(route)
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = name,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterVertically)
        )
    }
}