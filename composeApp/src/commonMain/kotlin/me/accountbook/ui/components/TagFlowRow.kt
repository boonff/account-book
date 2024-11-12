package me.accountbook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import me.accountbook.database.bean.Tag
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import me.accountbook.data.TestData.TagList
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagFlowRow(tags: List<Tag>) {
    var tagList by remember { mutableStateOf(tags) }
    var layoutCoordinatesList by remember { mutableStateOf(mutableSetOf<LayoutCoordinates>()) } // 在外部管理坐标列表

    FlowRow(
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tagList.forEachIndexed { index, tag ->
            DraggableTag(
                tag = tag,
                fromIndex = index,
                layoutCoordinatesList = layoutCoordinatesList, // 将坐标列表传递给子组件
                onDragEnd = { fromIndex, toIndex ->
                    tagList = tagList.toMutableList().apply {
                        val temp = this[fromIndex]
                        this[fromIndex] = this[toIndex]
                        this[toIndex] = temp
                    }
                },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}


@Composable
fun DraggableTag(
    tag: Tag,
    fromIndex: Int,
    layoutCoordinatesList: MutableSet<LayoutCoordinates>, // 接收传入的坐标列表
    onDragEnd: (fromIndex: Int, toIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                layoutCoordinatesList.add(coordinates) // 更新坐标列表
            }
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val toIndex = calculateTargetIndex(offset, layoutCoordinatesList)
                        println("tag1: " + tag.name + ", " + "tag2: " + TagList[toIndex].name)
                        onDragEnd(fromIndex, toIndex)
                        offset = Offset.Zero
                    }
                ) { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            }
            .clickable {
                println(tag.name + "被点击")
            }
    ) {
        TagCard(tag.name, modifier = Modifier)
    }
}


fun calculateTargetIndex(draggedOffset: Offset, tagsCoordinates: Set<LayoutCoordinates>): Int {
    // 根据拖动位置计算目标标签的索引
    var minDistance = Float.MAX_VALUE
    var targetIndex = 0

    tagsCoordinates.forEachIndexed { index, coordinates ->
        // 获取标签的中心位置
        val tagCenter = coordinates.positionInParent() + Offset(
            coordinates.size.width / 2f,
            coordinates.size.height / 2f
        )

        // 计算拖动位置与标签中心之间的距离
        val distance = (draggedOffset - tagCenter).getDistance()

        if (distance < minDistance) {
            minDistance = distance
            targetIndex = index // 找到最近的标签位置
        }
    }
    return targetIndex
}

@Composable
fun TagCard(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(8.dp)
        )
    }
}