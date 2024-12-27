package me.accountbook.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.dsl.module
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun <T : Any> ReorderableGrid(
    items: List<T>,
    keySelector: (T) -> Any,
    onMove: (Int, Int) -> Unit,
    columns: GridCells = GridCells.Adaptive(minSize = 150.dp),
    contentPadding: PaddingValues = PaddingValues(8.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    itemContent: @Composable ReorderableCollectionItemScope.(T, Boolean) -> Unit
) {
    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        onMove(from.index, to.index)
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = columns,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement
    ) {
        items(items, key = keySelector) { item ->
            ReorderableItem(
                reorderableLazyGridState,
                key = keySelector(item)
            ) { isDragging ->
                itemContent(item, isDragging)
            }
        }
    }
}

