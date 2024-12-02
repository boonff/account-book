package me.accountbook.ui.setting.sync

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.network.GitHubApiService
import me.accountbook.ui.common.components.DetailsPage
import me.accountbook.ui.setting.sync.viewmodel.AccountDetailViewModel
import me.accountbook.utils.SyncUtil
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncDetails(navHostController: NavHostController) {
    val viewModel: AccountDetailViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    DetailsPage(
        "同步设置",
        navHostController,
        reLoadData = {},
        content =
        {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!viewModel.isLogin())
                    Text(
                        text = "登录",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                viewModel.login()
                            }
                    )
                if (viewModel.isLogin()) {
                    Text(
                        text = "硬删除",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                scope.launch(Dispatchers.IO) {
                                    SyncUtil.hardDelete()
                                }
                            }
                    )
                    Text(
                        text = "退出登录",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                scope.launch(Dispatchers.IO) {
                                    viewModel.revokeAccessToken()
                                }
                            }
                    )
                }

            }
        }
    )
}

