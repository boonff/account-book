package me.accountbook.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import me.accountbook.ui.common.components.BasicDetails


@Composable
fun AccountDetails(navHostController: NavHostController) {
    BasicDetails(
        "账户管理",
        navHostController,
        isSynced = false,//未实现
        {},
        {
            AccountCard("中国银行", 10000.0)
        }
    )

}

@Composable
fun AccountCard(title: String, balance: Double, modifier: Modifier = Modifier) {
    Box(modifier) {
        Column(

        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = if (false) "￥$balance" else "*".repeat(9),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}