package me.accountbook.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.accountbook.ui.common.components.HorizontalScrollWithBar
import me.accountbook.ui.common.components.TagCard
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun ReorderTagbox() {
    val viewModel: ReorderTagboxViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadSortedTagbox()
    }

    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        viewModel.moveTagbox(from.index, to.index)
    }
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
            items(viewModel.tagbox, key = { it.tagbox_id }) {
                ReorderableItem(reorderableLazyGridState, key = it.tagbox_id) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Surface(
                        shadowElevation = elevation,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        TagCard(
                            name = it.name,
                            color = Color(it.color),
                            modifier = Modifier
                                .draggableHandle(
                                    onDragStopped = {
                                        scope.launch {
                                            viewModel.updatePosition()
                                        }
                                    }
                                )
                        )
                    }
                }
            }
        }
        FormBar()
    }
}


@Composable
fun FormBar() {
    val viewModel: ReorderTagboxViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // 增加按钮与输入栏间距
        ) {
            // 输入栏
            OutlinedTextField(
                value = viewModel.text,
                onValueChange = { newText ->
                    viewModel.text = newText
                },
                placeholder = { Text("输入标签名") }, // 添加占位符
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically)
            )
            // 按钮
            Button(
                onClick = {
                    scope.launch {
                        viewModel.insertTagbox(viewModel.text, Color(0x124357))
                        viewModel.loadSortedTagbox()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null,
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        viewModel.insertTagbox(viewModel.text, Color(0x124357))
                        viewModel.loadSortedTagbox()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null,
                )
            }
        }

        GradientBar(
            colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalScrollWithBar {
            Box(modifier = Modifier.width(40.dp).height(30.dp).background(Color.Magenta))
            Box(modifier = Modifier.width(1000.dp).height(30.dp))
        }
    }


}

