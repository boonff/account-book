package me.accountbook.koin

import me.accountbook.ui.home.viewmodel.EditTagboxViewModel
import me.accountbook.ui.home.viewmodel.FormBarViewModel
import me.accountbook.ui.home.viewmodel.DetailsTagboxViewModel
import me.accountbook.ui.setting.sync.viewmodel.AccountDetailViewModel
import me.accountbook.ui.setting.sync.viewmodel.SyncPointViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    single {
        OAuthConfig(
            port = 8080,
            rootDirectory = "/callback",
            clientId = "Ov23liXtj86dNazNUcLs",
            clientSecret = "0af720cec0feea32a6c07f6a9d56e5d7823d8a5c",
            oauthUrl = "https://github.com/login/oauth/authorize",
            oauthTokenUrl = "https://github.com/login/oauth/access_token",
        )
    }
    viewModel { SyncPointViewModel() }
    viewModel { DetailsTagboxViewModel() }
    viewModel { FormBarViewModel() }
    viewModel { EditTagboxViewModel() }
    viewModel { AccountDetailViewModel() }
}

data class OAuthConfig(
    val port: Int,
    val rootDirectory: String,
    val clientId: String,
    val clientSecret: String,
    val oauthUrl: String,
    val oauthTokenUrl: String,
)

fun OAuthConfig.getRedirectUri(): String {
    return "http://localhost:${this.port}${this.rootDirectory}"
}
