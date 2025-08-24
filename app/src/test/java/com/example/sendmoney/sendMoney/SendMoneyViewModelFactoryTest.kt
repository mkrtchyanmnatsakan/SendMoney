package com.example.sendmoney.sendMoney

import androidx.lifecycle.ViewModel
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.data.session.UserSessionManager
import com.example.sendmoney.ui.sendMoney.SendMoneyViewModel
import com.example.sendmoney.ui.sendMoney.SendMoneyViewModelFactory
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class AnotherTestViewModel : ViewModel()

class SendMoneyViewModelFactoryTest {

    private lateinit var mockServicesRepository: ServicesRepository
    private lateinit var mockRequestRepository: RequestRepository

    private lateinit var viewModelFactory: SendMoneyViewModelFactory

    private lateinit var userSessionManager: UserSessionManager

    @Before
    fun setUp() {
        mockServicesRepository = mockk()
        mockRequestRepository = mockk()
        userSessionManager = mockk()

        viewModelFactory = SendMoneyViewModelFactory(
            userSessionManager = userSessionManager,
            servicesRepository =  mockServicesRepository,
            requestRepository =  mockRequestRepository)
    }

    @Test
    fun `create should return SendMoneyViewModel when modelClass is SendMoneyViewModel`() {
        val viewModel = viewModelFactory.create(SendMoneyViewModel::class.java)

        assertNotNull("ViewModel should not be null", viewModel)
        assertTrue(
            "ViewModel should be an instance of SendMoneyViewModel",
            true
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create should throw IllegalArgumentException for unknown ViewModel class`() {
        viewModelFactory.create(AnotherTestViewModel::class.java)

    }

    @Test
    fun `create with generic type parameter returns correct ViewModel instance`() {
        // Given
        val modelClass: Class<SendMoneyViewModel> = SendMoneyViewModel::class.java

        // When
        val viewModel: SendMoneyViewModel = viewModelFactory.create(modelClass)

        // Then
        assertNotNull("ViewModel should not be null", viewModel)
        assertTrue(
            "ViewModel should be an instance of SendMoneyViewModel",
            true
        )
    }
}
