package me.accountbook.data.repository

import me.accountbook.data.local.AccountHelper
import me.accountbook.data.local.DatabaseHelper
import me.accountbook.data.model.SerAccount

class AccountRepository(dbHelper: AccountHelper) : DatabaseRepository<SerAccount>(dbHelper) {

}