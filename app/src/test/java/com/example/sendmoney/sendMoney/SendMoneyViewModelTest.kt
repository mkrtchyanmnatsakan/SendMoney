package com.example.sendmoney.sendMoney


import com.example.sendmoney.data.models.Service
import com.example.sendmoney.data.models.ServicesData
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.db.RequestEntity
import com.example.sendmoney.ui.sendMoney.SendMoneyViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMoneyViewModelTest {
    private val servicesRepository = mockk<ServicesRepository>()
    private val requestRepository = mockk<RequestRepository>()
    private lateinit var viewModel: SendMoneyViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        coEvery { servicesRepository.getServices() } returns ServicesData(
            title = mapOf("en" to "Send Money", "ar" to "إرسال الأموال"),
            services = listOf(
                Service(
                    label = mapOf("en" to "Bank Transfer"),
                    name = "bank_transfer",
                    providers = listOf()
                )
            )
        )
        coEvery { requestRepository.insertRequest(any()) } returns Unit
        viewModel = SendMoneyViewModel(servicesRepository, requestRepository)
    }

    @Test
    fun testServicesLoaded() = runTest {
        Assert.assertEquals(1, viewModel.services.value.services.size)
        Assert.assertEquals("Send Money", viewModel.services.value.title["en"])
    }

    @Test
    fun testSaveRequestWithEmptyFormData() = runTest {
        viewModel.saveRequest("Bank Transfer", "ABC Bank", 100.0, emptyMap())
        coVerify(exactly = 0) { requestRepository.insertRequest(any()) }
        Assert.assertFalse(viewModel.isSaved.value)
    }

    @Test
    fun testSaveRequest() = runTest {
        val serviceName = "Bank Transfer"
        val providerName = "ABC Bank"
        val amount = 100.0
        val formData = mapOf(
            "amount" to "100.0",
            "bank_account_number" to "1234567890",
            "firstname" to "Mnatsakan",
            "lastname" to "Mkrtchyan"
        )
        val expectedJson = Json.Default.encodeToString(formData)
        val expectedEntity = RequestEntity(
            serviceName = serviceName,
            providerName = providerName,
            amount = amount,
            jsonDetails = expectedJson
        )

        viewModel.saveRequest(serviceName, providerName, amount, formData)

        coVerify { requestRepository.insertRequest(expectedEntity) }
        Assert.assertTrue(viewModel.isSaved.value)
    }
}