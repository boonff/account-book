package me.accountbook.koin

import androidx.navigation.NavHostController
import me.accountbook.network.LoginManager
import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.FormBarViewModel
import me.accountbook.ui.home.viewmodel.HomeScreenViewModel
import me.accountbook.ui.home.viewmodel.ReorderTagboxViewModel
import me.accountbook.ui.home.viewmodel.TagDetailsViewModel
import me.accountbook.ui.setting.sync.viewmodel.SyncDetailViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule = module {
    single {
        OAuthConfig(
            port = 8080,
            rootDirectory = "/callback",
            clientId = "Ov23liXtj86dNazNUcLs",
            clientSecret = "0af720cec0feea32a6c07f6a9d56e5d7823d8a5c",
        )
    }

        viewModel { TagDetailsViewModel() }
        viewModel { ReorderTagboxViewModel(get()) }
        viewModel { HomeScreenViewModel(get()) }
        viewModel { FormBarViewModel() }
        viewModel { EditTagboxViewModel(get()) }
        viewModel { SyncDetailViewModel(get()) }
    }

data class OAuthConfig(
    val port: Int,
    val rootDirectory: String,
    val clientId: String,
    val clientSecret: String,
) : KoinComponent

fun OAuthConfig.getRedirectUri(): String {
    val config:OAuthConfig by inject()
    return "http//localhost:${config.port}${config.rootDirectory}"
}
