package me.accountbook.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ThemeColorBar() {
    // 获取当前主题的所有颜色
    val colors = colorScheme

    // 将所有颜色存储到一个列表中
    val colorList = listOf(
        "Primary" to colors.primary,
        "OnPrimary" to colors.onPrimary,
        "PrimaryContainer" to colors.primaryContainer,
        "OnPrimaryContainer" to colors.onPrimaryContainer,
        "Secondary" to colors.secondary,
        "OnSecondary" to colors.onSecondary,
        "SecondaryContainer" to colors.secondaryContainer,
        "OnSecondaryContainer" to colors.onSecondaryContainer,
        "Tertiary" to colors.tertiary,
        "OnTertiary" to colors.onTertiary,
        "TertiaryContainer" to colors.tertiaryContainer,
        "OnTertiaryContainer" to colors.onTertiaryContainer,
        "Background" to colors.background,
        "OnBackground" to colors.onBackground,
        "Surface" to colors.surface,
        "OnSurface" to colors.onSurface,
        "SurfaceVariant" to colors.surfaceVariant,
        "OnSurfaceVariant" to colors.onSurfaceVariant,
        "Error" to colors.error,
        "OnError" to colors.onError,
        "ErrorContainer" to colors.errorContainer,
        "OnErrorContainer" to colors.onErrorContainer,
        "Outline" to colors.outline
    )

    // 用 Column 或 LazyColumn 显示颜色
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(colorList) { (name, color) ->
            ColorItem(name, color)
        }
    }
}

@Composable
fun ColorItem(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(48.dp)
    ) {
        // 显示颜色条
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(color)
        )

        // 显示颜色名称
        Text(
            text = name,
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(backgroundColor = 0x000000)
@Composable
fun PreviewThemeColorBar() {
    ThemeColorBar()
}
