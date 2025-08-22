package com.example.sendmoney.ui.savedRequests

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sendmoney.R
import com.example.sendmoney.ui.theme.PurpleGrey40
import com.example.sendmoney.utils.LanguageManager

@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRequestsScreen(viewModel: SavedRequestsViewModel, navController: NavController) {
    val requests by viewModel.requests.collectAsState()
    var selectedJson by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0].language



    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.saved_requests),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController?.popBackStack()?.let {
                            if (!it) {
                                (context as? Activity)?.moveTaskToBack(true)
                            }
                        }
                    }) {
                        val currentLayoutDirection = LocalLayoutDirection.current
                        val backIcon = if (currentLayoutDirection == LayoutDirection.Rtl) {
                            Icons.AutoMirrored.Filled.KeyboardArrowRight
                        } else {
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft
                        }
                        Icon(backIcon, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    val (buttonText, nextLocale) = if (locale == "en") {
                        "🇸🇦 AR" to "ar"
                    } else {
                        "🇺🇸 EN" to "en"
                    }
                    TextButton(onClick = {
                        LanguageManager.setLocale(nextLocale)
                        LanguageManager.applyLocale(context, nextLocale)
                    }) {
                        Text(buttonText, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PurpleGrey40
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(requests) { request ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedJson = request.jsonDetails }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("ID: ${request.id}")
                            Text("Service: ${request.serviceName}")
                            Text("Provider: ${request.providerName}")
                            Text("Amount: ${request.amount} AED")
                        }
                    }
                }
            }
            if (selectedJson != null) {
                AlertDialog(
                    onDismissRequest = { selectedJson = null },
                    title = { Text(stringResource(R.string.request_details)) },
                    text = { Text(selectedJson!!) },
                    confirmButton = {
                        Button(onClick = { selectedJson = null }) {
                            Text(stringResource(R.string.close))
                        }
                    }
                )
            }
        }
    }
}