package me.accountbook.koin

import me.accountbook.ui.home.viewmodel.TagDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    viewModel { TagDetailsViewModel() }

}
