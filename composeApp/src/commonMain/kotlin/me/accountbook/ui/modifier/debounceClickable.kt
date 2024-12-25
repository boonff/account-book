package me.accountbook.ui.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.debounceClickable(
    enabled: Boolean = true,
    delayMs: Long = 1000,
    onClick: () -> Unit
): Modifier {
    val lastClickTime = remember { mutableStateOf(0L) } // 记录最后一次点击的时间

    return this.then(
        Modifier.clickable(enabled = enabled) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime.value >= delayMs) {
                lastClickTime.value = currentTime
                onClick()
            }
        }
    )
}