package me.accountbook.platform

import androidx.compose.runtime.Composable

actual fun getPlatform(): Platform = Platform.Desktop
@Composable
actual fun BasicPageVisible(): Boolean {
    return true
}

@Composable
actual fun getHomeLazyVerticalStaggeredGridColumns(): Int {
    return 2
}


