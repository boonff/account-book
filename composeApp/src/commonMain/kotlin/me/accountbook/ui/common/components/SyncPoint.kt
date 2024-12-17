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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.accountbook.data.manager.sync.SyncStateManager
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncPoint(
    syncStateViewModel: SyncStateManager,
    modifier: Modifier = Modifier,
    sync: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    LaunchedEffect(Unit){
        viewModel.initLogin()
    }
    Box(
        modifier = modifier.padding(end = 16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (syncStateViewModel.isLoading) CircularProgressIndicator(modifier = Modifier.size(16.dp))
            Box(modifier = Modifier
                .size(16.dp)
                .background(
                    color = if (viewModel.isLogin) syncStateViewModel.getColorForState() else Color.Gray,
                    shape = CircleShape
                )
                .clickable {
                    sync()
                }
            )
        }
    }
}
