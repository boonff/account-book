package me.accountbook

import android.content.res.Resources
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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