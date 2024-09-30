package me.accountbook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat


@Composable
fun InfoRow(
    color: Color,
    title: String = "null",
    subtitle: String = "null",
    save: Float,
) {
    val dollarSign = if (save < 0) "–￥ " else "￥ "
    val formattedAmount = formatAmount(save)
    Row(
        modifier = Modifier
            .height(68.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography
        ColorBar(
            color = color,
            modifier = Modifier
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier) {
            Text(text = title, style = typography.bodyLarge)
            Text(
                text = subtitle,
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dollarSign,
                style = typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = formattedAmount,
                style = typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.width(16.dp))

        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
    Line()
}

@Composable
fun CompactInfoRow(
    color: Color,
    save: Float
) {
    val dollarSign = if (save < 0) "–￥ " else "￥ "
    val formattedAmount = formatAmount(save)

    Row(
        modifier = Modifier.height(48.dp), // 调整高度以使其更小
        verticalAlignment = Alignment.CenterVertically
    ) {
        ColorBar(color = color)

        Spacer(Modifier.width(8.dp)) // 减少间隔

        // 显示金额
        Text(
            text = dollarSign + formattedAmount,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // 让金额文本占据剩余空间
        )
    }
}


/**
 * A vertical colored line that is used in a [InfoRow] to differentiate accounts.
 */
@Composable
private fun ColorBar(color: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .size(4.dp, 16.dp)
            .background(color = color)
    )
}

@Composable
fun Line(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.background
    )
}

fun formatAmount(amount: Float): String {
    return AmountDecimalFormat.format(amount)
}

private val AccountDecimalFormat = DecimalFormat("####")
private val AmountDecimalFormat = DecimalFormat("#,###.##")