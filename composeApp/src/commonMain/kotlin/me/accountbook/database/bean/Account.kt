package me.accountbook.database.bean

import java.math.BigDecimal

interface Account {
    val accountID: Int
    val accountName: String
    val platformName: String
    val balance: BigDecimal
    fun getAccountType(): String
}

data class BankCard (
    override val accountID: Int,
    override val accountName: String,
    override val platformName: String,
    override var balance:BigDecimal,

    ): Account {
    override fun getAccountType(): String ="Bank Card"
}

// 虚拟账户类
data class VirtualAccount(
    override val accountID: Int,
    override val accountName: String,
    override val platformName: String,
    override val balance: BigDecimal,
) : Account {
    override fun getAccountType(): String = "Virtual Account"
}