package com.example.sendmoney.ui.sendMoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.data.session.UserSessionManager

// Factory for SendMoneyViewModel

class SendMoneyViewModelFactory(
    private val userSessionManager: UserSessionManager,
    private val servicesRepository: ServicesRepository,
    private val requestRepository: RequestRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendMoneyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SendMoneyViewModel(
                userSessionManager, servicesRepository,
                requestRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}