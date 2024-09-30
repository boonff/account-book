package me.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize

// actual 实现
actual fun getPlatform(): Platform = Platform.Desktop

@Composable
actual fun getHomeLazyVerticalStaggeredGridColumns(): Int {
    return 2
}
