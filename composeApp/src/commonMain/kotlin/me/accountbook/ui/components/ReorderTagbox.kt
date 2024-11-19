package me.accountbook.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper
import org.koin.compose.koinInject
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun TagReorder(tags: List<Tagbox>) {
    val dbHelper: DatabaseHelper = koinInject()

    var tagNames by remember { mutableStateOf(tags.map { it.name }) }
    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        tagNames = tagNames.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }


    LaunchedEffect(Unit) {
        val tagboxs = dbHelper.queryAllTagBox()
        tagNames = tagboxs.map { it.name }
    }

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
                        color = Color(tags[index].color),
                        modifier = Modifier
                            .draggableHandle()
                    )
                }
            }
        }
    }
}
