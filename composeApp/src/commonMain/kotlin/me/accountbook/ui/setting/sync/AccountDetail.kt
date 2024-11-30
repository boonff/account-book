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
                if (!viewModel.isLogin)
                //登录按钮
                    Text(
                        text = "获得授权令牌",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                //viewModel.serialization()
                                viewModel.initToken()

                            }
                    )

                if (viewModel.isLogin) {
                    Row {
                        Text(
                            text = "加载用户名",
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable {
                                    scope.launch(Dispatchers.IO) {
                                        viewModel.fetchGitHubUserInfo()
                                    }
                                }
                        )
                        //用户名加载动画
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            "User: ${viewModel.userInfo}",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
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
                        text = "销毁令牌",
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

