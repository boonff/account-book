package me.accountbook.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DetailsPage(
    title: String,
    navController: NavHostController,
    content:@Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        content()
    }

}