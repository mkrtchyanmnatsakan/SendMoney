package com.example.sendmoney.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sendmoney.ui.login.LoginScreen
import com.example.sendmoney.ui.login.LoginViewModel
import com.example.sendmoney.ui.savedRequests.SavedRequestsScreen
import com.example.sendmoney.ui.savedRequests.SavedRequestsViewModel
import com.example.sendmoney.ui.sendMoney.SendMoneyScreen
import com.example.sendmoney.ui.sendMoney.SendMoneyViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN_SCREEN) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel,
                onSuccessfulLoginNavigation = {
                    navController.navigate(Routes.SEND_MONEY_SCREEN) {
                        popUpTo(Routes.LOGIN_SCREEN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.SEND_MONEY_SCREEN) {
            val sendMoneyViewModel: SendMoneyViewModel = hiltViewModel()
            LaunchedEffect(key1 = sendMoneyViewModel.navigateToLogin) {
                sendMoneyViewModel.navigateToLogin.collect { event ->
                    if (event) {
                        navController.navigate(Routes.LOGIN_SCREEN) {
                            popUpTo(Routes.SEND_MONEY_SCREEN) { inclusive = true } // Clear back stack to sendMoney
                            launchSingleTop = true
                        }
                    }
                }
            }

            SendMoneyScreen(
                viewModel = sendMoneyViewModel,
                navController = navController
            )
        }

        composable(Routes.SAVED_REQUESTS_SCREEN) {
            val savedRequestsViewModel: SavedRequestsViewModel = hiltViewModel()
            SavedRequestsScreen(
                viewModel = savedRequestsViewModel,
                navController = navController
            )
        }
    }
}
