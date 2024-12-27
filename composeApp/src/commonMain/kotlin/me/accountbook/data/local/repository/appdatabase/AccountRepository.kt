package me.accountbook.data.local.repository.appdatabase

import me.accountbook.data.local.helper.appdatabase.AccountHelper
import me.accountbook.database.Account

class AccountRepository(dbHelper: AccountHelper) : AppDatabaseRepository<Account>(dbHelper) {

}