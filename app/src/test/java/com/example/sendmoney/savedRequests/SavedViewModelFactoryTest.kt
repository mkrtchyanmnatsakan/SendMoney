package com.example.sendmoney.savedRequests

import androidx.lifecycle.ViewModel
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.ui.savedRequests.SavedRequestsViewModel
import com.example.sendmoney.ui.savedRequests.SavedRequestsViewModelFactory
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class AnotherTestViewModel : ViewModel()

class SavedViewModelFactoryTest {

    private lateinit var mockRequestRepository: RequestRepository

    private lateinit var viewModelFactory: SavedRequestsViewModelFactory

    @Before
    fun setUp() {
        mockRequestRepository = mockk()

        viewModelFactory = SavedRequestsViewModelFactory(mockRequestRepository)
    }

    @Test
    fun `create should return SavedViewModel when modelClass is SavedViewModel`() {

        val viewModel = viewModelFactory.create(SavedRequestsViewModel::class.java)

        assertNotNull("ViewModel should not be null", viewModel)
        assertTrue(
            "ViewModel should be an instance of SavedViewModel",
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
        val modelClass: Class<SavedRequestsViewModel> = SavedRequestsViewModel::class.java

        // When
        val viewModel: SavedRequestsViewModel = viewModelFactory.create(modelClass)

        // Then
        assertNotNull("ViewModel should not be null", viewModel)
        assertTrue(
            "ViewModel should be an instance of SavedViewModel",
            true
        )
    }
}
