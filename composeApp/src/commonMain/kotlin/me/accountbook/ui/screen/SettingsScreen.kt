package me.accountbook.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.accountbook.data.TestData.cardDataList
import me.accountbook.ui.components.BarCard
import me.accountbook.ui.components.FunCart
import kotlin.random.Random

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
    )
    Column(
        modifier = Modifier
            .fillMaxSize(), // 让列填满整个可用空间
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = "Settings Screen",
            style = MaterialTheme.typography.headlineSmall
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            item {
                FunCart("test")
                {
                        BarCard(title = "f", save = 100.0f)
                        BarCard(title = "f", save = 100.0f) }
            }
            item {
                FunCart("test")
                {
                    BarCard(title = "f", save = 100.0f)
                }
            }
            item {
                FunCart("test")
                {
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                }
            }
            item {
                FunCart("test")
                {
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                }
            }
            item {
                FunCart("test")
                {
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)

                }
            }
            item {
                FunCart("test")
                {
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                    BarCard(title = "f", save = 100.0f)
                }
            }

        }

    }
}