package me.accountbook.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import me.accountbook.data.TestData.TagList
import me.accountbook.ui.components.DetailsPage
import me.accountbook.ui.components.TagReorder

@Composable
fun TagDetails(navHostController: NavHostController) {
    DetailsPage("标签管理", navHostController){
            TagReorder(TagList)

    }

}