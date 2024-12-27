package me.accountbook.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.accountbook.ui.common.components.viewmodel.SyncPointViewModel

//需要大改
@Composable
fun SyncPoint(
    viewModel: SyncPointViewModel,
    modifier: Modifier = Modifier,
    sync: () -> Unit,
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val syncState by viewModel.syncState.collectAsState()

    Box(
        modifier = modifier.padding(end = 16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(16.dp))
            Box(modifier = Modifier
                .size(16.dp)
                .background(
                    color = viewModel.getColorForState(syncState),
                    shape = CircleShape
                )
                .clickable {
                    sync()
                }
            )
        }
    }
}