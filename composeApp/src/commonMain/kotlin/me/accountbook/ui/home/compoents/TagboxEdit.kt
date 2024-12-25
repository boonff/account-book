package me.accountbook.ui.home.compoents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.accountbook.ui.home.viewmodel.TagboxEditViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TagboxEdit(showDialog: MutableState<Boolean>) {
    val viewModel: TagboxEditViewModel = koinViewModel()
    Dialog(onDismissRequest = { showDialog.value = false }) {
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
                            viewModel.softDelete(viewModel.uuid)
                            showDialog.value = false
                        }
                    }
                }

                ColorBar { newColor ->
                    viewModel.color = newColor
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(
                        onClick = {
                            showDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                        )
                    ) {
                        Text("取消")
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                viewModel.updateName(
                                    viewModel.name,
                                    viewModel.uuid
                                )
                                viewModel.updateColor(
                                    viewModel.color,
                                    viewModel.uuid
                                )
                                showDialog.value = false
                                println(
                                    "按钮确定 showDialog.value:${showDialog.value}" +
                                            "id:${showDialog}"
                                )
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