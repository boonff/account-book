package me.accountbook.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

object DeviceUtils {
    @Composable
    fun isTablet(): Boolean {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val screenHeight = configuration.screenHeightDp
        // 判断是否为平板设备
        return minOf(screenWidth, screenHeight) >= 1280
    }

    @Composable
    fun isPortrait(): Boolean {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val screenHeight = configuration.screenHeightDp
        // 判断设备是否为竖屏，宽高比大于 4/3 视为竖屏
        return screenHeight / screenWidth.toFloat() > 4 / 3
    }
    
    @Composable
    fun isLandscape(): Boolean {
        return isTablet() || isPortrait()
    }
}
