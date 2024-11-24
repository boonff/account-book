package me.accountbook.koin

import androidx.navigation.NavHostController
import me.accountbook.network.LoginManager
import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.FormBarViewModel
import me.accountbook.ui.home.viewmodel.HomeScreenViewModel
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import me.accountbook.ui.home.viewmodel.TagDetailsViewModel
import me.accountbook.ui.setting.sync.viewmodel.SyncDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule = module {
    single(named("port")) { 8080 }
    single(named("rootDirectory")) { "/callback" }
    single {
        LoginManager(
            clientId = "Ov23liXtj86dNazNUcLs",
            clientSecret = "0af720cec0feea32a6c07f6a9d56e5d7823d8a5c",
            redirectUri = "http://localhost:8080/callback"
        )
    }
    viewModel { TagDetailsViewModel() }
    viewModel { ReorderTagboxViewModel(get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { FormBarViewModel() }
    viewModel { EditTagboxViewModel(get()) }
    viewModel { SyncDetailViewModel(get()) }
}
