package com.example.sendmoney.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LoginViewModel() : ViewModel() {

    private val _error = MutableStateFlow( false)
    val error = _error.asStateFlow()


    fun login(email: String, password: String, onSuccess: () -> Unit) {
        //Requirements: Use a hardcoded username and password (e.g., username: testuser, password: password123).

        if (email == "testuser" && password == "password123") {
            _error.value = false
            onSuccess()
        } else {
            _error.value = true
        }
    }

}