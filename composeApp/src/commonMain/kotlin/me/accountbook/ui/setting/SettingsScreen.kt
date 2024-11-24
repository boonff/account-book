package me.accountbook.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import me.accountbook.ui.common.components.BasicPage
import me.accountbook.ui.navigation.Screen

@Composable
fun SettingsScreen(isLandscape: Boolean, navHostController: NavHostController) {
    BasicPage(isLandscape = isLandscape, title = "设置") {
        Column {
            optionsCard(
                name = "同步",
                Icons.Outlined.Sync,
                Screen.SyncDetails.route,
                navHostController = navHostController
            )
        }
    }
}

@Composable
fun optionsCard(
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