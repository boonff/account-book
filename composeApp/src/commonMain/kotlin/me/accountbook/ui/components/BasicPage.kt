package me.accountbook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.accountbook.ui.screen.title


@Composable
fun BasicPage(
    isLandscape: Boolean = false,
    title: String,
    content: @Composable () -> Unit //主要内容
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background) // 使用主题背景色
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(), // 让列填满整个可用空间
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (isLandscape) {
                title("首页")
            }
            content()
        }
    }
}
