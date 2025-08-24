package com.example.sendmoney.extension

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController

fun ComponentActivity.setupRootScreenMinimizeOnBackPress(
    navController: NavController,
    rootScreenRoute: String
) {
    onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentRoute = navController.currentDestination?.route
            val isAtRootOfTargetScreen = (currentRoute == rootScreenRoute &&
                    navController.previousBackStackEntry == null)

            if (isAtRootOfTargetScreen) {
                this@setupRootScreenMinimizeOnBackPress.moveTaskToBack(true)
            } else {
                if (!navController.popBackStack()) {
                    this@setupRootScreenMinimizeOnBackPress.finish()
                }
            }
        }
    })
}
