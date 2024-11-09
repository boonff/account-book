package me.accountbook.platform

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import me.accountbook.platform.Platform
import me.accountbook.utils.DeviceUtils

// actual 实现
actual fun getPlatform(): Platform = Platform.Android


@Composable
actual fun getHomeLazyVerticalStaggeredGridColumns(): Int {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = DeviceUtils.isLandscape(screenWidth, screenHeight)

    return if (isLandscape)
        1
    else
        2
}