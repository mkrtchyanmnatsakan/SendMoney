package com.example.sendmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sendmoney.data.session.UserSessionManager
import com.example.sendmoney.extension.setupRootScreenMinimizeOnBackPress
import com.example.sendmoney.ui.navigation.AppNavHost
import com.example.sendmoney.ui.navigation.Routes
import com.example.sendmoney.ui.theme.SendMoneyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSessionManager: UserSessionManager

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SendMoneyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    var initialLoginStateResolved by remember { mutableStateOf<Boolean?>(null) }

                    LaunchedEffect(key1 = Unit) {
                        initialLoginStateResolved = userSessionManager.checkInitialLoginState()
                    }

                    if (initialLoginStateResolved == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {

                        AppNavHost(
                            navController = navController,
                            startDestination = if (initialLoginStateResolved == true)
                                Routes.SEND_MONEY_SCREEN else Routes.LOGIN_SCREEN
                        )
                    }
                    setupCustomBackPressLogic()
                }
            }
        }

    }

    private fun setupCustomBackPressLogic() {

        setupRootScreenMinimizeOnBackPress(navController, Routes.SEND_MONEY_SCREEN)

    }
}