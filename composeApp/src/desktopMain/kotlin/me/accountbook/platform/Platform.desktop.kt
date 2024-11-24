package me.accountbook.platform

import androidx.compose.runtime.Composable
import java.awt.Desktop
import java.net.URI

actual fun getPlatform(): Platform = Platform.Desktop
@Composable
actual fun BasicPageVisible(): Boolean {
    return true
}

@Composable
actual fun getHomeLazyVerticalStaggeredGridColumns(): Int {
    return 2
}


