package me.accountbook.utils

object DeviceUtils {
    fun isTablet(screenWidth: Int, screenHeight: Int): Boolean {
        // 判断是否为平板设备
        return minOf(screenWidth, screenHeight) >= 600
    }

    fun isPortrait(screenWidth: Int, screenHeight: Int): Boolean {
        // 判断设备是否为竖屏，宽高比大于 4/3 视为竖屏
        return screenHeight / screenWidth > 4 / 3
    }
    
    fun isLandscape(screenWidth: Int, screenHeight: Int): Boolean {
        return isTablet(screenWidth, screenHeight) || DeviceUtils.isPortrait(screenWidth, screenHeight)
    }
}
