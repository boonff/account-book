package me.accountbook.ui.setting.sync

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.ui.common.components.DetailsPage
import me.accountbook.ui.setting.sync.viewmodel.SyncDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncDetails(navHostController: NavHostController) {
    val viewModel:SyncDetailViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    DetailsPage("同步设置", navHostController) {
        Column {
            Icon(
                imageVector = Icons.Outlined.Sync,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "sync",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        scope.launch(Dispatchers.IO) {
                            viewModel.serialization()
                            viewModel.login()
                        }
                    }
            )
        }

    }
}