package me.accountbook.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun FunCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit = {}, // 点击事件的回调
    modifier: Modifier = Modifier,
    syncPoint: @Composable () -> Unit,
    content: @Composable () -> Unit, //主要内容
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.5f)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            FunCardTopBar(title, icon, syncPoint) { onClick() }
            content()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FunCardTopBar(
    title: String,
    icon: ImageVector = Icons.Rounded.Warning,
    syncPoint: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // 将子元素分布在两端
        verticalAlignment = Alignment.CenterVertically // 垂直居中对齐
    ) {
        Row {
            Icon(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier.size(24.dp), // 图标大小设置为48dp
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            syncPoint()
        }

        // 将箭头放在最右侧
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "箭头",
            modifier = Modifier.size(24.dp)
        )

    }
}

