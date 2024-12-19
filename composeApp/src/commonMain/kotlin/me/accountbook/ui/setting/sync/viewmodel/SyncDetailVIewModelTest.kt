package me.accountbook.ui.setting.sync.viewmodel

import androidx.lifecycle.ViewModel
import me.accountbook.data.repository.TagboxRepository
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncDetailVIewModelTest : ViewModel(), KoinComponent {
    val tagboxRepository: TagboxRepository by inject()

}