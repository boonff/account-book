package me.accountbook.database.bean

import java.math.BigDecimal
import java.util.*

data class Transaction(
    val transactionId: Int,           // 交易ID
    val cardID: Int,              // 关联的银行卡ID
    val amount: BigDecimal,              // 交易金额
    val date: Date,                      // 交易日期
    val description: String,             // 交易描述
)

