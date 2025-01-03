package me.accountbook.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import me.accountbook.platform.Platform
import me.accountbook.utils.DeviceUtils
import org.koin.compose.koinInject

actual fun getPlatform(): Platform = Platform.Android

//是否显BasicPage标题
@Composable
actual fun BasicPageVisible(): Boolean {
    return DeviceUtils.isLandscape()
}

//屏幕不同状态时主页LazyVerticalStaggeredGrid行数
@Composable
actual fun getHomeLazyVerticalStaggeredGridColumns(): Int {
    return if (DeviceUtils.isLandscape()) 1 else 2
}
