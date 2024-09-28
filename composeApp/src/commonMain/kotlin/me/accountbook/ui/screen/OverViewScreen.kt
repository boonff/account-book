package com.example.bookkeeping.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookkeeping.R
import com.example.bookkeeping.data.TestData
import com.example.bookkeeping.ui.chart.MarkerDonutChart
import com.example.bookkeeping.ui.components.InfoRow
import com.example.bookkeeping.ui.components.formatAmount

@Composable
fun OverViewScreen() {
    // 读取测试变量，根据总金额、目标金额、紧急储蓄金额
    val proportionColors = listOf(
        TestData.SaveColor, TestData.WarnColor, TestData.TargetColor, TestData.OverColor
    )
    val markerColors = listOf(
        TestData.WarnColor, TestData.TargetColor
    )
    var saveDetail = SaveDetail()
    saveDetail.saveProportion(TestData.Save, TestData.TargetSave, TestData.WarnSave)
    val proportions = saveDetail.getProportions()
    val markers = saveDetail.getMarkers()
    val saves = saveDetail.getSaves()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box(Modifier.padding(16.dp)) {
            MarkerDonutChart(
                proportions = proportions,
                proportionColors = proportionColors,
                markers = markers,
                markerColors = markerColors,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = formatAmount(saves[0] + saves[3]),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card {
            Column(modifier = Modifier.padding(12.dp)) {
                InfoRow (title = "计划内储蓄", save = saves[0], color = proportionColors[0])
                if (saves[1] > 0)
                    InfoRow(title = "紧急储蓄差", save = saves[1], color = proportionColors[1])
                if (saves[2] > 0)
                    InfoRow(title = "目标储蓄差", save = saves[2], color = proportionColors[2])
                if (saves[3] > 0)
                    InfoRow(title = "额外的储蓄", save = saves[3], color = proportionColors[3])
            }
        }
    }

}

class SaveDetail {
    private var proportions: List<Float> = emptyList()
    private var markers: List<Float> = emptyList()
    private var saves: List<Float> = emptyList()
    fun saveProportion(
        save: Float,
        targetSave: Float,
        warnSave: Float
    ) {
        val lostWarnSave = maxOf(warnSave - save, 0f)
        val lostTargetSave = maxOf(targetSave - maxOf(warnSave, save), 0f)
        val overSave = maxOf(save - targetSave, 0f)
        val sum = save + lostWarnSave + lostTargetSave + overSave
        proportions =
            listOf(save / sum, lostWarnSave / sum, lostTargetSave / sum, overSave / sum)
        saves = listOf(save, lostWarnSave, lostTargetSave, overSave)
        val max_save = maxOf(save, targetSave)
        markers = listOf(warnSave / max_save, targetSave / max_save)
    }

    fun getProportions(): List<Float> {
        return proportions
    }

    fun getMarkers(): List<Float> {
        return markers
    }

    fun getSaves(): List<Float> {
        return saves
    }

}


@Preview
@Composable
fun ShowOverViewScreen() {
    OverViewScreen()
}
