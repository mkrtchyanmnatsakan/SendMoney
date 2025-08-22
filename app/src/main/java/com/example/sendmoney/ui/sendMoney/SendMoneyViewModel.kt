package com.example.sendmoney.ui.sendMoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sendmoney.data.models.ServicesData
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.db.RequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class SendMoneyViewModel(
    private val servicesRepository: ServicesRepository,
    private val requestRepository: RequestRepository
) : ViewModel() {
    private val _services = MutableStateFlow(ServicesData(emptyMap(),
        emptyList()))
    val services = _services.asStateFlow()
    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    init {
        viewModelScope.launch {
            _services.value = withContext(Dispatchers.IO) { // Execute on IO thread
                servicesRepository.getServices()
            }
        }
    }


    fun saveRequest(serviceName: String, providerName: String, amount: Double,
                    formData: Map<String, String>) {
        viewModelScope.launch {
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
        }
    }

    fun setSavedValue(value: Boolean) {
        _isSaved.value = value
    }
}