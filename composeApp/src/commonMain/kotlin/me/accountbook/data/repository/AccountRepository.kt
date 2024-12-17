package me.accountbook.data.repository

import me.accountbook.data.local.helper.AccountHelper
import me.accountbook.database.Account

class AccountRepository(dbHelper: AccountHelper) : AppDatabaseRepository<Account>(dbHelper) {

}