package me.accountbook.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

//设备类型
sealed class Platform {
    abstract val name: String

    data object Android : Platform() {
        override val name: String = "Android"
    }

    data object Desktop : Platform() {
        override val name: String = "Java ${System.getProperty("java.version")}"
    }

}

//获取当前设备类型
expect fun getPlatform(): Platform
//是否显BasicPage标题
@Composable
expect fun BasicPageVisible():Boolean


//屏幕不同状态时主页LazyVerticalStaggeredGrid行数
@Composable
expect fun getHomeLazyVerticalStaggeredGridColumns(): Int
