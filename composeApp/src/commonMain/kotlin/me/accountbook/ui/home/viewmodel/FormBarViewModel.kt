package me.accountbook.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FormBarViewModel : ViewModel() {
    var text by mutableStateOf("")
}