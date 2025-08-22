package com.example.sendmoney.login

import androidx.lifecycle.SavedStateHandle
import com.example.sendmoney.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain

class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: LoginViewModel

    companion object {
        private const val ERROR_KEY = "login_error_key"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        viewModel = LoginViewModel(savedStateHandle)
    }

    @Test
    fun testLoginSuccess() {
        //Requirements: Use a hardcoded username and password (e.g., username: testuser, password: password123).

        var success = false
        viewModel.login("testuser", "password123") { success = true }
        Assert.assertTrue(success)
        Assert.assertFalse("Error state should be false after successful login", viewModel.error.value)
        Assert.assertEquals("SavedStateHandle should have false for ERROR_KEY after successful login", false, savedStateHandle.get<Boolean>(ERROR_KEY))
    }

    @Test
    fun testLoginFailure() {
        viewModel.login("wrong", "wrong") {}
        Assert.assertTrue("Error state should be true after failed login", viewModel.error.value)
        Assert.assertEquals("SavedStateHandle should have true for ERROR_KEY after failed login", true, savedStateHandle.get<Boolean>(ERROR_KEY))
    }

    @Test
    fun testSetErrorValue() {

        viewModel.setErrorValue(true)
        Assert.assertTrue("error.value should be true after setErrorValue(true)", viewModel.error.value)
        Assert.assertEquals("SavedStateHandle should have true for ERROR_KEY after setErrorValue(true)", true, savedStateHandle.get<Boolean>(ERROR_KEY))


        viewModel.setErrorValue(false)
        Assert.assertFalse("error.value should be false after setErrorValue(false)", viewModel.error.value)
        Assert.assertEquals("SavedStateHandle should have false for ERROR_KEY after setErrorValue(false)", false, savedStateHandle.get<Boolean>(ERROR_KEY))
    }
}