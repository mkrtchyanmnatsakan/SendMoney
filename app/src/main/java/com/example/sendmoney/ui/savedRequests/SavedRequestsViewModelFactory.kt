package com.example.sendmoney.ui.savedRequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sendmoney.data.repository.RequestRepository

// Factory for SavedRequestsViewModel
class SavedRequestsViewModelFactory(
    private val requestRepository: RequestRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedRequestsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavedRequestsViewModel(requestRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}