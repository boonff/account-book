package me.accountbook.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import me.accountbook.ui.home.ColorPalette.colors
import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditTagbox() {
    val viewModel: EditTagboxViewModel = koinViewModel()
    val reorderTagboxViewModel: ReorderTagboxViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    if (viewModel.isPopupVisible) {
        Dialog(onDismissRequest = { viewModel.togglePopupVisible() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TagboxCard(
                            viewModel.name, viewModel.color,
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            deleteButton(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                            ) {
                                scope.launch {
                                    viewModel.deleteTagboxById(viewModel.tagboxId)
                                    reorderTagboxViewModel.loadSortedTagbox()
                                }
                                viewModel.togglePopupVisible()
                            }
                        }
                    }

                    colorBar { newColor ->
                        viewModel.color = newColor
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(
                            onClick = {
                                viewModel.togglePopupVisible()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.onTertiary,
                            )
                        ) {
                            Text("取消")
                        }
                        Box(modifier = Modifier.fillMaxWidth()){
                            Button(
                                onClick = {
                                    scope.launch {
                                        viewModel.updateTagboxName(
                                            viewModel.name,
                                            viewModel.tagboxId
                                        )
                                        viewModel.updateTagboxColor(
                                            viewModel.color,
                                            viewModel.tagboxId
                                        )
                                        reorderTagboxViewModel.loadSortedTagbox()
                                    }
                                    viewModel.togglePopupVisible()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                            ) {
                                Text("确定")
                            }
                        }

                    }

                }
            }
        }
    }
}

object ColorPalette {
    val colors = listOf(
        Color(0xFFFF0000), // 红色
        Color(0xFFFF7F00), // 橙色
        Color(0xFFFFD700), // 黄色
        Color(0xFF008000), // 绿色
        Color(0xFF0000FF), // 蓝色
        Color(0xFF4B0082), // 靛蓝
        Color(0xFFEE82EE), // 紫色
        Color(0xFFFF1493)  // 深粉色
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun colorBar(
    modifier: Modifier = Modifier,
    onClick: (color: Color) -> Unit
) {

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (color in colors) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .background(color, RoundedCornerShape(8.dp))
                    .clickable {
                        onClick(color)
                    }
            )
        }
    }
}

@Composable
fun deleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,  // 背景色
            contentColor = MaterialTheme.colorScheme.onError,  // 内容色，按钮文字颜色
            disabledContainerColor = MaterialTheme.colorScheme.surface,  // 禁用状态下的背景色
            disabledContentColor = MaterialTheme.colorScheme.onSurface,  // 禁用状态下的文字颜色
        )
    ) {
        Text(
            text = "删除",
            color = MaterialTheme.colorScheme.onError,  // 设置文字颜色为 MaterialTheme 中的 onError 颜色
            style = MaterialTheme.typography.bodyMedium,  // 设置文字样式
        )
    }
}