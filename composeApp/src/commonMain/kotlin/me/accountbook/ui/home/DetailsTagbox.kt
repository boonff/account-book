package me.accountbook.ui.home

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.accountbook.ui.common.components.DetailsPage
import me.accountbook.ui.home.viewmodel.TagDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailsTagbox(navHostController: NavHostController) {
    val viewModel: TagDetailsViewModel = koinViewModel()


    DetailsPage("标签管理", navHostController) {
        ReorderTagbox()
    }
}

@Composable
fun Title(title:String){
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun Line(){
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.background
    )
}