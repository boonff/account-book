package me.accountbook.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountCard(
    title: String = "Anonymous",
    balance: Double,
    modifier: Modifier = Modifier // 添加 modifier 参数
) {
    Card(
        modifier = modifier
            .fillMaxSize(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom=4.dp)
            )

            //CompactInfoRow(color = TestData.SaveColor, balance = balance)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "银行描述",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

