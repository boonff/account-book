package me.accountbook.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import sh.calvin.reorderable.ReorderableItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import me.accountbook.database.bean.Tag
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun TagReorder(tags: List<Tag>) {

    var tagNames by remember { mutableStateOf(tags.map { it.name }) }

    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        tagNames = tagNames.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(max = 256.dp)
                .wrapContentHeight()
                .fillMaxHeight(),
            state = lazyGridState,
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(tagNames, key = { _, it -> it }) { index, it ->
                ReorderableItem(reorderableLazyGridState, key = it) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Surface(
                        shadowElevation = elevation,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        TagCard(
                            name = it,
                            color = tags[index].color,
                            modifier = Modifier
                                .draggableHandle()
                        )
                    }
                }
            }
        }
    }
}