package me.accountbook.ui.setting.sync.viewmodel

import androidx.lifecycle.ViewModel
import me.accountbook.data.local.repository.appdatabase.TagboxRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncDetailVIewModelTest : ViewModel(), KoinComponent {
    val tagboxRepository: TagboxRepository by inject()

}