package me.accountbook.platform

import androidx.compose.runtime.Composable

// actual 实现
actual fun getPlatform(): Platform = Platform.Desktop

@Composable
actual fun getHomeLazyVerticalStaggeredGridColumns(): Int {
    return 2
}

