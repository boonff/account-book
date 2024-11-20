package me.accountbook.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.common.components.TagCard
import org.koin.compose.koinInject
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun ReorderTagbox(
    tagbox: List<Tagbox>,
    isShow: MutableState<Boolean>
) {
    var tagboxList by remember { mutableStateOf(tagbox) }
    val scope = rememberCoroutineScope()
    val text = remember { mutableStateOf("") }
    val dbHelper: DatabaseHelper = koinInject()
    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        tagboxList = tagboxList.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    LaunchedEffect(Unit) {
        val allTags = dbHelper.queryAllTagBox()
        tagboxList = allTags
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .heightIn(max = 256.dp)
            .wrapContentHeight()
            .fillMaxHeight(),
        state = lazyGridState,
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(tagboxList, key = { _, it -> it.position }) { index, tagbox ->
            ReorderableItem(reorderableLazyGridState, key = tagbox.position) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                Surface(
                    shadowElevation = elevation,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    TagCard(
                        name = tagbox.name,
                        color = Color(tagbox.color),
                        modifier = Modifier
                            .draggableHandle()
                    )
                }
            }
        }

        item {
            Box {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(8.dp)
                ) {
                    if (!isShow.value)
                        Text(
                            text = "add",
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    isShow.value = true
                                }
                        )
                    else
                        FormBar(
                            text = text,
                            onClick = {
                                scope.launch {
                                    // Add new tag to the database
                                    dbHelper.insertTagBox(text.value, 0x932720)
                                    // Update the tagboxs list directly
                                    tagboxList = dbHelper.queryAllTagBox()
                                }
                            }
                        )
                }
            }
        }
    }
}


@Composable
fun FormBar(
    text: MutableState<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = text.value,
            onValueChange = { newText ->
                text.value = newText
            },
            modifier = Modifier
                .weight(0.8f)
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .weight(0.2f)
        ) {
            Text("+")
        }

    }

}
