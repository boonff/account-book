package me.accountbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

sealed class Platform {
    abstract val name: String

    data object Android : Platform() {
        override val name: String = "Android"
    }

    data object Desktop : Platform() {
        override val name: String = "Java ${System.getProperty("java.version")}"
    }

}

// getPlatform.kt
expect fun getPlatform(): Platform

@Composable
expect fun getHomeLazyVerticalStaggeredGridColumns(): Int
