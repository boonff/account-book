package me.accountbook.ui.setting.sync

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.accountbook.ui.common.components.BasicDetails
import me.accountbook.ui.setting.sync.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncDetails(navHostController: NavHostController) {
    val viewModel: LoginViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    BasicDetails(
        "同步设置",
        navHostController,
        syncPoint = {},
        content =
        {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!viewModel.isLogin)
                    Text(
                        text = "登录",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                viewModel.login()
                            }
                    )
                if (viewModel.isLogin) {
                    Text(
                        text = "硬删除",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                scope.launch(Dispatchers.IO) {
                                    //SyncUtil.hardDelete() 未实现
                                }
                            }
                    )
                    Text(
                        text = "退出登录",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                viewModel.logout()
                            }
                    )
                }

            }
        }
    )
}

