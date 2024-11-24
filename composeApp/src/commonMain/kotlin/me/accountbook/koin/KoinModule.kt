package me.accountbook.koin

import androidx.navigation.NavHostController
import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.FormBarViewModel
import me.accountbook.ui.home.viewmodel.HomeScreenViewModel
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import me.accountbook.ui.home.viewmodel.TagDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    viewModel { TagDetailsViewModel() }
    viewModel { ReorderTagboxViewModel(get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { FormBarViewModel() }
    viewModel { EditTagboxViewModel(get()) }
}
