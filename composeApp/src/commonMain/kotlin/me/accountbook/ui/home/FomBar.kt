package me.accountbook.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.ui.home.ColorPalette.colors
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random

@Composable
fun FormBar(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
) {
    val viewModel: ReorderTagboxViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    Box(modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.text,
                onValueChange = { newText ->
                    viewModel.text = newText
                },
                placeholder = { Text("输入标签名") },
                modifier = Modifier

                    .weight(0.6f)
                    .align(Alignment.CenterVertically)
            )
            //添加tagbox的按钮
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val random = Random.nextInt(0, colors.size)
                        viewModel.insertTagbox(viewModel.text, colors[random])
                        viewModel.loadSortedTagbox() //添加tagbox后触发tagboxs的更新，
                        viewModel.text = ""
                        onAddClick() //回调函数，目前的作用是添加元素后将LazyVerticalGrid滚动到末尾
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

        }

    }
}