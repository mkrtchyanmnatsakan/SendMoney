package com.example.sendmoney.ui.sendMoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sendmoney.data.models.ServicesData
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.data.session.UserSessionManager
import com.example.sendmoney.db.RequestEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val servicesRepository: ServicesRepository,
    private val requestRepository: RequestRepository
) : ViewModel() {
    private val _services = MutableStateFlow(
        ServicesData(
            emptyMap(),
            emptyList()
        )
    )
    val services = _services.asStateFlow()
    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    private val _navigateToLogin = MutableStateFlow(false)
    val navigateToLogin = _navigateToLogin.asStateFlow()

    init {
        viewModelScope.launch {
            _services.value = withContext(Dispatchers.IO) {
                servicesRepository.getServices()
            }
        }
    }


    fun saveRequest(
        serviceName: String,
        providerName: String,
        amount: Double,
        formData: Map<String, String>
    ) {
        viewModelScope.launch {
            if (serviceName.isNotEmpty() && providerName.isNotEmpty() && amount > 0 &&
                formData.isNotEmpty()) {
                val jsonDetails = Json.encodeToString(formData)
                requestRepository.insertRequest(
                    RequestEntity(
                        serviceName = serviceName,
                        providerName = providerName,
                        amount = amount,
                        jsonDetails = jsonDetails
                    )
                )
                _isSaved.value = true
            } else {
                _isSaved.value = false
            }
        }
    }

    fun setSavedValue(value: Boolean) {
        _isSaved.value = value
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            try {
                userSessionManager.clearSessionData()
                _navigateToLogin.value = true
            } catch (e: Exception) {

            }
        }
    }
}