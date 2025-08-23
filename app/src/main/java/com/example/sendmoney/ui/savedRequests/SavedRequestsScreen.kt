package com.example.sendmoney.ui.savedRequests

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sendmoney.R
import com.example.sendmoney.ui.component.CommonTopAppBarCenter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRequestsScreen(viewModel: SavedRequestsViewModel, navController: NavController) {
    val requests by viewModel.requests.collectAsState()
    var selectedJsonString by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0].language

    val prettyJsonFormatter = remember { Json { prettyPrint = true } }

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
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                stringResource(R.string.request_id) +
                                    " ${request.id}")
                            Text("${stringResource(R.string.service_name)} " +
                                    request.serviceName
                            )
                            Text(
                                stringResource(R.string.provider_name) +
                                    " ${request.providerName}")
                            Text("${stringResource(R.string.amount)} " +
                                    "${request.amount}" +
                                    " ${stringResource(R.string.aed)}")

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { selectedJsonString = request.jsonDetails }) {
                                    Text(stringResource(R.string.view_details))
                                }
                            }
                        }
                    }
                }
            }
            if (selectedJsonString != null) {
                var formattedJsonToShow by remember(selectedJsonString) {
                    mutableStateOf("")
                }

                try {
                    val jsonElement = Json.parseToJsonElement(selectedJsonString!!)
                    formattedJsonToShow = prettyJsonFormatter
                        .encodeToString(JsonElement.serializer(), jsonElement)
                } catch (e: Exception) {
                    formattedJsonToShow = selectedJsonString ?: "Error displaying JSON details."
                }

                AlertDialog(
                    onDismissRequest = { selectedJsonString = null },
                    title = { Text(stringResource(R.string.request_details)) },
                    text = {
                        val scrollState = rememberScrollState()
                        Text(
                            text = formattedJsonToShow,
                            modifier = Modifier
                                .heightIn(max = 400.dp)
                                .verticalScroll(scrollState),
                            fontSize = 12.sp
                        )
                    },
                    confirmButton = {
                        Button(onClick = { selectedJsonString = null }) {
                            Text(stringResource(R.string.close))
                        }
                    }
                )
            }
        }
    }
}
