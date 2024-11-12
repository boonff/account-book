package me.accountbook.ui.components

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.rememberReorderableLazyGridState

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
actual fun ReorderTag(list: List<String>) {
    val view = LocalView.current

    var li by remember { mutableStateOf(list) }

    val lazyGridState = rememberLazyGridState()
    val reorderableLazyGridState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        li = li.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .heightIn(max = 500.dp),
        state = lazyGridState,
        columns = GridCells.Fixed(3), // 设置为 3 列
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(li, key = { it }) { tag ->
            ReorderableItem(reorderableLazyGridState, key = tag) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp, label = "")

                Surface(shadowElevation = elevation) {
                    Row {
                        // 将拖动逻辑放在TagCard本身
                        TagCard(
                            name = tag,
                            modifier = Modifier
                                .draggableHandle(
                                    onDragStarted = {
                                        view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                                    },
                                    onDragStopped = {
                                        view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}
