package com.example.bookkeeping.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private const val DividerLengthInDegrees = 1.8f

@Composable
fun MarkerDonutChart(
    modifier: Modifier = Modifier,
    proportions: List<Float>,
    proportionColors: List<Color>,
    markers: List<Float>,
    markerColors: List<Color>
) {
    val stroke = with(LocalDensity.current) { Stroke(5.dp.toPx()) }

    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = -90f

        // Draw proportions
        proportions.forEachIndexed { index, proportion ->
            if (proportion > 0) {
                val sweep = proportion * 360f
                drawArc(
                    color = proportionColors[index],
                    startAngle = startAngle + DividerLengthInDegrees / 2,
                    sweepAngle = sweep - DividerLengthInDegrees,
                    topLeft = topLeft,
                    size = size,
                    useCenter = false,
                    style = stroke
                )
                startAngle += sweep
            }
        }

        // Draw markers
        markers.forEachIndexed { index, markerPosition ->
            val markerAngle = 360f * markerPosition - 90f + 90f
            rotate(markerAngle) {
                val path = Path().apply {
                    moveTo(halfSize.width, halfSize.height - innerRadius - 5) // Point of the arrow
                    lineTo(halfSize.width - 10, halfSize.height - innerRadius - 25)
                    lineTo(halfSize.width + 10, halfSize.height - innerRadius - 25)
                    close()
                }
                drawPath(path, markerColors[index])
            }
        }
    }
}

