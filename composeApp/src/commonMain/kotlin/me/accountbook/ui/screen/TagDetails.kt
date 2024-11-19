package me.accountbook.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import me.accountbook.database.Tagbox
import me.accountbook.sqldelight.DatabaseHelper
import me.accountbook.ui.components.DetailsPage
import me.accountbook.ui.components.SearchBar
import me.accountbook.ui.components.TagCard
import org.koin.compose.koinInject
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun TagDetails(navHostController: NavHostController) {
    val dbHelper: DatabaseHelper = koinInject()
    var tagbox by remember { mutableStateOf<List<Tagbox>>(emptyList()) }
    LaunchedEffect(Unit) {
        tagbox = dbHelper.queryAllTagBox()
    }

    DetailsPage("标签管理", navHostController) {
        TagReorder(tagbox)

    }

}

@Composable
fun TagReorder(tags: List<Tagbox>) {
    val scope = rememberCoroutineScope()        //获取当前作用域
    var text by remember { mutableStateOf("") }
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

    // 追踪弹出视图的显示状态
    var showPopup by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
            item {
                TagCard(
                    name = "add",
                    color = Color(0x072133),
                    modifier = Modifier
                        .clickable {
                            showPopup = true
                        }
                )
            }
        }

        // 如果点击了底部按钮，则显示弹出视图
        if (showPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        showPopup = false
                    }
            )
            // 弹出的视图
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter), // 让弹出视图居底部,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                SearchBar(
                    text = text,
                    onTextChange = { newText ->
                        text = newText
                    },
                    onSearchClick = {
                        scope.launch {
                            dbHelper.insertTagBox(text, 0x789834)
                        }
                    },
                )
            }

        }
    }
}
