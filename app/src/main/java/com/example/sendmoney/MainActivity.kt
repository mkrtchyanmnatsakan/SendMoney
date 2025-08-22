package com.example.sendmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.db.AppDatabase
import com.example.sendmoney.ui.login.LoginScreen
import com.example.sendmoney.ui.login.LoginViewModel
import com.example.sendmoney.ui.savedRequests.SavedRequestsScreen
import com.example.sendmoney.ui.savedRequests.SavedViewModel
import com.example.sendmoney.ui.savedRequests.SavedViewModelFactory
import com.example.sendmoney.ui.sendMoney.SendMoneyScreen
import com.example.sendmoney.ui.sendMoney.SendMoneyViewModel
import com.example.sendmoney.ui.sendMoney.SendMoneyViewModelFactory
import com.example.sendmoney.ui.theme.SendMoneyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SendMoneyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val servicesRepository = ServicesRepository(applicationContext)
                    val requestRepository = RequestRepository(AppDatabase.getDatabase(applicationContext).requestDao())
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(navController, viewModel<LoginViewModel>())
                        }
                        composable("sendMoney") {
                            val sendMoneyViewModel: SendMoneyViewModel = viewModel(
                                factory = SendMoneyViewModelFactory(
                                    servicesRepository,
                                    requestRepository
                                )
                            )
                            SendMoneyScreen(sendMoneyViewModel, navController)
                        }
                        composable("savedRequests") {
                            val savedViewModel: SavedViewModel = viewModel(
                                factory = SavedViewModelFactory(requestRepository)
                            )
                            SavedRequestsScreen(savedViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}