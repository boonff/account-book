package me.accountbook.ui.setting.sync

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.accountbook.ui.setting.sync.viewmodel.SyncPointViewModel
import org.koin.compose.koinInject

@Composable
fun SyncPoint(
    isSynced: Boolean,
    modifier: Modifier = Modifier,
    syncData: () -> Unit,
) {
    Box(
        modifier = modifier.padding(end = 16.dp)
    ) {
        val viewmodel: SyncPointViewModel = koinInject()

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!viewmodel.syncSuccess)
                Text(
                    "同步失败"
                )
            if (viewmodel.isLoading) CircularProgressIndicator(modifier = Modifier.size(16.dp))
            Box(modifier = Modifier
                .size(16.dp)
                .background(
                    if (isSynced) Color.Green else Color.Red,
                    shape = CircleShape
                )
                .clickable {
                    syncData()
                }
            )
        }
    }
}
