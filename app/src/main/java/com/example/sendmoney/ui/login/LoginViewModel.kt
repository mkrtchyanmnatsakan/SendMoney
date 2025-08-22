package com.example.sendmoney.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val ERROR_KEY = "login_error_key"
    }


    private val _error = MutableStateFlow(savedStateHandle.get<Boolean>(ERROR_KEY) ?: false)
    val error = _error.asStateFlow()


    fun login(email: String, password: String, onSuccess: () -> Unit) {
        //Requirements: Use a hardcoded username and password (e.g., username: testuser, password: password123).

        if (email == "testuser" && password == "password123") {
            _error.value = false
            savedStateHandle[ERROR_KEY] = false
            onSuccess()
        } else {
            _error.value = true
            savedStateHandle[ERROR_KEY] = true
        }
    }

    fun setErrorValue(value: Boolean) {
        _error.value = value
        savedStateHandle[ERROR_KEY] = value
    }

}