package me.accountbook.koin

import me.accountbook.data.manager.domain.TagboxManager
import me.accountbook.data.manager.sync.AccountSyncManager
import me.accountbook.data.manager.sync.TagboxSyncManager
import me.accountbook.data.repository.AccountRepository
import me.accountbook.data.repository.AppDatabaseRepository
import me.accountbook.data.repository.TableTimestampRepository
import me.accountbook.data.repository.TagboxRepository
import me.accountbook.database.Account
import me.accountbook.database.Tagbox
import me.accountbook.network.manager.RepositoryManager
import me.accountbook.ui.home.viewmodel.TagboxEditViewModel
import me.accountbook.ui.home.viewmodel.TagboxFormBarViewModel
import me.accountbook.ui.home.viewmodel.TagboxDataViewModel
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel
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
    single { RepositoryManager() }

    single<AppDatabaseRepository<Tagbox>> { TagboxRepository(get()) }
    single<AppDatabaseRepository<Account>> { AccountRepository(get()) }
    single { TagboxRepository(get()) }
    single { AccountRepository(get()) }
    single { TableTimestampRepository(get()) }

    single { TagboxManager() }

    single { TagboxSyncManager }
    single { AccountSyncManager }


    viewModel { TagboxDataViewModel() }
    viewModel { TagboxFormBarViewModel() }
    viewModel { TagboxEditViewModel() }
    viewModel { LoginViewModel() }


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
