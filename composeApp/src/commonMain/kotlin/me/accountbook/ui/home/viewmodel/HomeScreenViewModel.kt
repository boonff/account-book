package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.accountbook.database.Account
import me.accountbook.database.DatabaseHelper
import me.accountbook.ui.common.viewmodel.BaseTagboxVIewModel

class HomeScreenViewModel(dbHelper: DatabaseHelper) : BaseTagboxVIewModel(dbHelper) {
    var accounts by mutableStateOf<List<Account>>(emptyList())

    suspend fun loadAccount() {
        accounts = dbHelper.queryAllAccount()

    }
}