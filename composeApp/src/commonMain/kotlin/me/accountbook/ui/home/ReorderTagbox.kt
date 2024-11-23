package me.accountbook.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun ReorderTagbox() {
    val hapticFeedback = LocalHapticFeedback.current
    val viewModel: ReorderTagboxViewModel = koinViewModel()
    val editTagboxViewModel: EditTagboxViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        viewModel.moveTagbox(from.index, to.index)
    }
    // 获取当前的输入法控制器
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit)
    {
        viewModel.loadSortedTagbox()
    }
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
                        .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .background(Color.Transparent)

                ) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .wrapContentHeight(),
                        state = lazyGridState,
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        items(viewModel.tagboxs, key = { it.tagbox_id }) {
                            ReorderableItem(
                                reorderableLazyGridState,
                                key = it.tagbox_id
                            ) { isDragging ->
                                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                                Surface(
                                    shadowElevation = elevation,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    TagboxCard(
                                        name = it.name,
                                        color = Color(it.color),
                                        modifier = Modifier
                                            .longPressDraggableHandle(
                                                onDragStarted = {
                                                    hapticFeedback.performHapticFeedback(
                                                        HapticFeedbackType.LongPress
                                                    )
                                                },
                                                onDragStopped = {
                                                    scope.launch {
                                                        viewModel.updatePosition()
                                                    }
                                                }
                                            )
                                            .clickable {
                                                editTagboxViewModel.togglePopupVisible()
                                                editTagboxViewModel.initByTagbox(it)
                                            }
                                    )
                                }


                            }
                        }

                    }
                }

            }
            FormBar {
                scope.launch {
                    val lastIndex = viewModel.tagboxs.size - 1
                    lazyGridState.scrollToItem(lastIndex)
                }
            }
        }
    }
    EditTagbox()
}



