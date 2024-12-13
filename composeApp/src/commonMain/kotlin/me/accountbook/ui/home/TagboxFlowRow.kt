package me.accountbook.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.accountbook.data.model.SerTagbox

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowTagbox(
    tagbox: List<SerTagbox>,
) {
    FlowRow(
        modifier = Modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tagbox.forEach { it ->
            TagboxCard(it.name, Color(it.color))
        }
    }
}

@Composable
fun TagboxCard(name: String, color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier){
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = color) // 使用 CardDefaults 设置背景色
        ) {
            Text(
                text = name,
                color = Color.Black,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }

}

@Composable
fun VarTagboxCard(name: String, color: Color, modifier: Modifier = Modifier) {
    var isShow by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
        ) {
            TagboxCard(
                name,
                color,
                modifier
                    .clickable {
                        isShow = !isShow
                    }
            )
        }
        Box(
            modifier = Modifier
                .height(8.dp)
                .width(8.dp)
                .alpha(if (isShow) 1f else 0f)
                .background(MaterialTheme.colorScheme.error)
                .align(Alignment.CenterEnd)
        ) {
            Text(
                text = "-",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 6.sp
            )
        }
    }

}
