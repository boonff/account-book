package me.accountbook.ui.components

import androidx.compose.runtime.Composable
import me.accountbook.database.bean.Tag

@Composable
expect fun ReorderTag(list: List<String>)