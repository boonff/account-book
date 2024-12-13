package me.accountbook.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.accountbook.platform.BasicPageVisible
import me.accountbook.ui.setting.sync.SyncPoint


@Composable
fun BasicScreen(
    title: String,
    isSynced: Boolean,
    syncData: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            if (BasicPageVisible()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 16.dp, start = 16.dp)
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        SyncPoint(
                            isSynced = isSynced,
                            modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp)
                        ) { syncData() }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth()) {
                    SyncPoint(
                        isSynced = isSynced,
                        modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp)
                    ) { syncData() }
                }
            }
            Box(modifier = Modifier.padding(8.dp)) {
                content()
            }

        }
    }
}


