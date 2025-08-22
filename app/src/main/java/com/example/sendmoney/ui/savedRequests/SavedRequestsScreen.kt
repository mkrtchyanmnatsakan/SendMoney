package com.example.sendmoney.ui.savedRequests

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sendmoney.R
import com.example.sendmoney.ui.component.CommonTopAppBarCenter

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
            CommonTopAppBarCenter(
                title = stringResource(R.string.saved_requests),
                navController = navController,
                currentLocale = locale
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