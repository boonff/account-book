package com.example.bookkeeping.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.bookkeeping.data.TestData
import com.example.bookkeeping.database.bean.Tag
import com.example.bookkeeping.database.bean.Transaction
import com.example.bookkeeping.ui.chart.DonutChart
import com.example.bookkeeping.ui.components.formatAmount
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

//支出基类，
class TransactionsScreen {
    private val selectedOption = mutableStateOf<DateType>(DateType.Daily)
    //需要从数据库读入的参数
    private var transactions: List<Transaction> = emptyList()
    private var limit: Double = 0.0

    //处理后的值
    private var saves: List<Double> = emptyList()
    private var tags: List<Tag> = emptyList()
    private var proportions: List<Float> = emptyList()
    private var colors: List<Color> = emptyList()
    private var over: Float = 0.0f

    enum class DateType {
        Daily,
        Weekly,
        Monthly,
        Yearly,
    }

    @Composable
    fun Display() {
        detail(selectedOption.value)
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .weight(4f)
                ) {
                    DonutChart(
                        proportions = proportions,
                        colors = colors,
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                    )
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "消费占比",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = formatAmount(over),
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    FrequencySelection{it -> detail(it)}
                }
            }
        }
    }

    @Composable
    fun FrequencySelection(onOptionSelected: (DateType) -> Unit) {
        // 单选框选项
        val options = listOf(
            DateType.Daily to "日",
            DateType.Weekly to "周",
            DateType.Monthly to "月",
            DateType.Yearly to "年"
        )

        Column {
            options.forEach { (value, label) ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption.value == value,
                        onClick = {
                            selectedOption.value = value
                            onOptionSelected(value)
                        }
                    )
                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }


    //计算环形图所需数据的方法
    protected fun detail(dateType: DateType) {
        //在数据库中读取参数
        when (dateType) {
            DateType.Daily -> {
                transactions = TestData.Transactions
                transactions = transactions.filter { thisDay(it.date) }
                limit = TestData.day_limit
            }

            DateType.Weekly -> {
                transactions = TestData.Transactions
                transactions = transactions.filter { thisWeek(it.date) }
                limit = TestData.day_limit
            }

            DateType.Monthly -> {
                transactions = TestData.Transactions
                transactions = transactions.filter { thisMonth(it.date) }
                limit = TestData.day_limit
            }

            DateType.Yearly -> {
                transactions = TestData.Transactions
                transactions = transactions.filter { thisYear(it.date) }
                limit = TestData.day_limit
            }
        }


        transactions = transactions.sortedBy { it.save }
        saves = transactions.map { it.save }
        tags = transactions.map { it.tag }
        colors = tags.map { it.color }

        val save_sum = saves.sum()
        if (save_sum < limit) {
            proportions = saves.map { (it / limit).toFloat() }

            proportions = proportions + (1 - (save_sum / limit).toFloat())
            colors = colors + Color.Gray
        } else
            proportions = saves.map { (it / save_sum).toFloat() }
        over = (save_sum / limit).toFloat()
    }

    fun thisDay(date: Date): Boolean {
        val calendar = Calendar.getInstance().apply { set(2024, 7, 12) }
        val currentDate = calendar.get(Calendar.DATE)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.time = date
        val dateDate = calendar.get(Calendar.DATE)
        val dateMonth = calendar.get(Calendar.MONTH)
        val datetYear = calendar.get(Calendar.YEAR)

        return currentDate == dateDate && currentMonth == dateMonth && currentYear == datetYear
    }

    fun thisMonth(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.time = date
        val dateMonth = calendar.get(Calendar.MONTH)
        val dateYear = calendar.get(Calendar.YEAR)

        return dateMonth == currentMonth && dateYear == currentYear
    }

    fun thisYear(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.time = date
        val dateYear = calendar.get(Calendar.YEAR)

        return dateYear == currentYear
    }

    fun thisWeek(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val currentWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.time = date
        val dateWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
        val dateYear = calendar.get(Calendar.YEAR)

        return dateWeekOfYear == currentWeekOfYear && dateYear == currentYear
    }

}

@Preview
@Composable
fun showTransactionScreen() {
    val transactionScreen = TransactionsScreen()
    transactionScreen.Display()
}