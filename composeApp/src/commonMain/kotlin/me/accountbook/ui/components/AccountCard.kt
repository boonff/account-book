package me.accountbook.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.accountbook.data.TestData

@Composable
fun BarCard(
    title: String = "Anonymous",
    save: Float,
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

            CompactInfoRow(color = TestData.SaveColor, save = save)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "银行描述",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CardList(cards: List<Pair<String, Float>>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 256.dp), // 设置每个卡片的最小宽度
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(cards.size) { index ->
            val cardData = cards[index]
            BarCard(title = cardData.first, save = cardData.second)
        }
    }
}
