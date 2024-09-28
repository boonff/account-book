package com.example.bookkeeping.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    proportions: List<Float>,
    proportionColors: List<Color>,
    markers: List<Float>,
    markerColors: List<Color>
) {
    val stroke = with(LocalDensity.current) { Stroke(1.dp.toPx()) }
    val barWidth = with(LocalDensity.current) { 20.dp.toPx() }
    val barSpacing = with(LocalDensity.current) { 10.dp.toPx() }

    Canvas(modifier) {
        val totalHeight = size.height
        val totalWidth = size.width

        var currentOffset = 0f

        // Draw bars
        proportions.forEachIndexed { index, proportion ->
            val barHeight = proportion * totalHeight
            drawRect(
                color = proportionColors[index],
                topLeft = Offset(currentOffset, totalHeight - barHeight),
                size = Size(barWidth, barHeight),
                style = stroke
            )
            currentOffset += barWidth + barSpacing
        }

        // Draw markers
        markers.forEachIndexed { index, markerPosition ->
            val markerOffset = markerPosition * totalHeight
            drawLine(
                color = markerColors[index],
                start = Offset(0f, totalHeight - markerOffset),
                end = Offset(totalWidth, totalHeight - markerOffset),
                strokeWidth = stroke.width
            )
        }
    }
}
