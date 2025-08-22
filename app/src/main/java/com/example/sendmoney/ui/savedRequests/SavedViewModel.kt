package com.example.sendmoney.ui.savedRequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.db.RequestEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    private val _requests = MutableStateFlow<List<RequestEntity>>(emptyList())
    val requests = _requests.asStateFlow()

    init {
        viewModelScope.launch {
            requestRepository.getAllRequests().collect { _requests.value = it.reversed() }
        }
    }
}