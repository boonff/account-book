package me.accountbook.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun FunCart(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit, //主要内容
    onClick: () -> Unit = {}, // 点击事件的回调
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(8.dp), // 设置圆角
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)//外边框
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            FunChartTopBar(title, icon) { onClick() }
            content()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FunChartTopBar(
    title: String,
    icon: ImageVector = Icons.Rounded.Warning,
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

