package me.accountbook.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun ReorderTagbox() {
    val viewModel: ReorderTagboxViewModel = koinViewModel()
    val editTagboxViewModel: EditTagboxViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit)
    {
        viewModel.loadSortedTagbox()
    }

    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        viewModel.moveTagbox(from.index, to.index)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .fillMaxHeight()
                    .weight(1f),
                state = lazyGridState,
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.tagboxs, key = { it.tagbox_id }) {
                    ReorderableItem(reorderableLazyGridState, key = it.tagbox_id) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                        Surface(
                            shadowElevation = elevation,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            TagboxCard(
                                name = it.name,
                                color = Color(it.color.toULong()),
                                modifier = Modifier
                                    .draggableHandle(
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
            FormBar()
        }
        EditTagbox()
    }

}

