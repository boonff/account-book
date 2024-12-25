package me.accountbook.ui.home.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import me.accountbook.ui.common.components.BasicDetails
import me.accountbook.ui.common.components.ReorderableGrid
import me.accountbook.ui.common.components.SyncPoint
import me.accountbook.ui.common.components.viewmodel.SyncPointViewModel
import me.accountbook.ui.home.compoents.TagboxCard
import me.accountbook.ui.home.compoents.TagboxEdit
import me.accountbook.ui.home.compoents.TagboxFormBar
import me.accountbook.ui.home.viewmodel.TagboxDetailViewModel
import me.accountbook.ui.home.viewmodel.TagboxEditViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TagboxDetail(navHostController: NavHostController) {
    val viewModel: TagboxDetailViewModel = koinViewModel()
    val syncPointViewModel = SyncPointViewModel(viewModel.syncStateManager)


    val tagboxEditViewModel: TagboxEditViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    val tagboxList by viewModel.tagboxList.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val isClickable = remember { mutableStateOf(true) } // 防抖状态

    data class Item(val id: Int, val text: String, val size: Int)
    BasicDetails("标签管理",
        navHostController,
        syncPoint = {
            SyncPoint(syncPointViewModel) {
                viewModel.sync()
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.shapes.medium
                            )
                            .background(Color.Transparent)
                    ) {
                        ReorderableGrid(
                            items = tagboxList,
                            keySelector = { it.uuid },
                            onMove = { from, to ->
                                viewModel.moveTagbox(from, to)
                            },
                        ) { tagbox, isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                            Surface(
                                shadowElevation = elevation,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {
                                TagboxCard(
                                    name = tagbox.name,
                                    color = Color(tagbox.color),
                                    modifier = Modifier
                                        .clickable {
                                            if (isClickable.value) {
                                                isClickable.value = false
                                                scope.launch {
                                                    showDialog.value = true
                                                    tagboxEditViewModel.uuid = tagbox.uuid
                                                    tagboxEditViewModel.name = tagbox.name
                                                    tagboxEditViewModel.color =
                                                        Color(tagbox.color)
                                                    kotlinx.coroutines.delay(300)
                                                    isClickable.value = true
                                                }
                                            }
                                        }
                                        .draggableHandle(
                                            onDragStarted = {},
                                            onDragStopped = {
                                                viewModel.updatePosition()
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
                TagboxFormBar {
                }
            }
        }
        if (showDialog.value) {
            TagboxEdit(showDialog)
        }
    }
}