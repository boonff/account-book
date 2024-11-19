package me.accountbook.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Preview
@Composable
fun PreviewSearchBar(){
    SearchBar(
        text = "null",
        onSearchClick = {},
        onTextChange = {}
    )
}