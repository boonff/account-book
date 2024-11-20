package me.accountbook.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import me.accountbook.ui.home.TextFieldAdd

@Preview
@Composable
fun PreviewSearchBar(){
    TextFieldAdd(
        text = "null",
        onSearchClick = {},
        onTextChange = {}
    )
}