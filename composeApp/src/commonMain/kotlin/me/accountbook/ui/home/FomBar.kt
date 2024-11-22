package me.accountbook.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FormBar() {
    val viewModel: ReorderTagboxViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // 增加按钮与输入栏间距
        ) {
            // 输入栏
            OutlinedTextField(
                value = viewModel.text,
                onValueChange = { newText ->
                    viewModel.text = newText
                },
                placeholder = { Text("输入标签名") }, // 添加占位符
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically)
            )
            // 按钮
            Button(
                onClick = {
                    scope.launch {
                        viewModel.insertTagbox(viewModel.text, Color(0x124357))
                        viewModel.loadSortedTagbox()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null,
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        viewModel.insertTagbox(viewModel.text, Color(0x124357))
                        viewModel.loadSortedTagbox()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null,
                )
            }
        }


    }


}