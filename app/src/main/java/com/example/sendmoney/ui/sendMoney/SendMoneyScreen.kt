package com.example.sendmoney.ui.sendMoney

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sendmoney.R
import com.example.sendmoney.data.models.Provider
import com.example.sendmoney.data.models.Service
import com.example.sendmoney.data.repository.RequestRepository
import com.example.sendmoney.data.repository.ServicesRepository
import com.example.sendmoney.db.AppDatabase
import com.example.sendmoney.ui.theme.PurpleGrey40
import com.example.sendmoney.ui.theme.SendMoneyAppTheme
import com.example.sendmoney.utils.LanguageManager
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(viewModel: SendMoneyViewModel, navController: NavController) {
    val services by viewModel.services.collectAsState()
    var selectedService by remember { mutableStateOf<Service?>(null) }
    var selectedProvider by remember { mutableStateOf<Provider?>(null) }
    val fields = selectedProvider?.required_fields ?: emptyList()
    val formData = remember { mutableStateMapOf<String, String>() }
    val errors = remember { mutableStateMapOf<String, String>() }
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0].language
    var showSuccess by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.isSaved.collectLatest { if (it) showSuccess = true }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = services.title[locale] ?: stringResource(R.string.app_name),
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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))
            // Service Dropdown
            var serviceExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = serviceExpanded,
                onExpandedChange = { serviceExpanded = !serviceExpanded }
            ) {
                TextField(
                    value = selectedService?.label?.get(locale) ?: stringResource(R.string.select_service),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.select_service)
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = serviceExpanded,
                    onDismissRequest = { serviceExpanded = false }
                ) {
                    services.services.forEach { service ->
                        DropdownMenuItem(
                            text = { Text(service.label?.get(locale) ?: service.name) },
                            onClick = {
                                selectedService = service
                                selectedProvider = null
                                formData.clear()
                                errors.clear()
                                serviceExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Provider Dropdown
            AnimatedVisibility(visible = selectedService != null) {
                var providerExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = providerExpanded,
                    onExpandedChange = { providerExpanded = !providerExpanded }
                ) {
                    TextField(
                        value = selectedProvider?.name ?: stringResource(R.string.select_provider),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.select_provider)
                            )
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = providerExpanded,
                        onDismissRequest = { providerExpanded = false }
                    ) {
                        selectedService?.providers?.forEach { provider ->
                            DropdownMenuItem(
                                text = { Text(provider.name) },
                                onClick = {
                                    selectedProvider = provider
                                    formData.clear()
                                    errors.clear()
                                    providerExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Dynamic Fields
            LazyColumn {
                items(fields) { field ->
                    when (field.type) {
                        "text", "msisdn" -> {
                            TextField(
                                value = formData[field.name] ?: "",
                                onValueChange = { formData[field.name] = it.trim() },
                                label = { Text(field.label[locale] ?: "") },
                                placeholder = { Text(field.placeholder?.get(locale) ?: "") },
                                isError = errors.containsKey(field.name),
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = if (field.type == "msisdn") KeyboardType.Phone else KeyboardType.Text)
                            )
                        }
                        "number" -> {
                            TextField(
                                value = formData[field.name] ?: "",
                                onValueChange = { formData[field.name] = it.trim() },
                                label = { Text(field.label[locale] ?: "") },
                                placeholder = { Text(field.placeholder?.get(locale) ?: "") },
                                isError = errors.containsKey(field.name),
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        "option" -> {
                            var selectedOption by remember { mutableStateOf("") }
                            field.options?.let { options ->
                                Text(text = field.label[locale] ?: "", style = MaterialTheme.typography.bodyMedium)
                                options.forEach { option ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedOption == option.name,
                                            onClick = {
                                                selectedOption = option.name
                                                formData[field.name] = option.name
                                            }
                                        )
                                        Text(option.label)
                                    }
                                }
                            }
                        }
                        "date" -> {
                            val calendar = remember { Calendar.getInstance() }
                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _: DatePicker, year: Int, month: Int, day: Int ->
                                    formData[field.name] = String.format("%04d-%02d-%02d", year, month + 1, day)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            TextField(
                                value = formData[field.name] ?: "",
                                onValueChange = {},
                                label = { Text(field.label[locale] ?: "") },
                                placeholder = { Text(field.placeholder?.get(locale) ?: "") },
                                isError = errors.containsKey(field.name),
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                ),
                                trailingIcon = {
                                    IconButton(onClick = { datePickerDialog.show() }) {
                                        Icon(
                                            imageVector = Icons.Filled.DateRange,
                                            contentDescription = "Calendar"
                                        )
                                    }
                                }
                            )
                        }
                    }
                    if (errors[field.name] != null) {
                        Text(
                            text = errors[field.name]!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            // Submit Button
            Button(
                onClick = {
                    errors.clear()
                    fields.forEach { field ->
                        showValidationError = false
                        val value = formData[field.name] ?: ""
                        if (field.validation.isNotEmpty() && !value.matches(Regex(field.validation))) {
                            errors[field.name] = field.validation_error_message?.get(locale) ?: "Invalid input"
                        } else if (value.isEmpty() && field.validation_error_message != null) {
                            errors[field.name] = field.validation_error_message[locale] ?: "Required"
                        }
                    }
                    if (errors.isEmpty() && fields.isNotEmpty()) {
                        showValidationError = false
                        viewModel.saveRequest(
                            serviceName = selectedService?.label?.get(locale) ?: selectedService?.name ?: "",
                            providerName = selectedProvider?.name ?: "",
                            amount = formData["amount"]?.toDoubleOrNull() ?: 0.0,
                            formData = formData
                        )
                    }else{
                        showValidationError = fields.isEmpty()
                    }
                },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E55F6)),
                modifier = Modifier.fillMaxWidth().padding(start = 80.dp, end = 80.dp, top = 32.dp, bottom = 32.dp),
            ) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = stringResource(R.string.submit))
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Language Switch Button removed from here
            if(showValidationError){
                AlertDialog(
                    onDismissRequest = { showValidationError = false},
                    title = { Text(stringResource(R.string.warning)) },
                    text = { Text(stringResource(R.string.warning_message)) },
                    confirmButton = {
                        Button(onClick = { showValidationError = false }) {
                            Text(stringResource(R.string.close))
                        }
                    }
                )
            }
            if (showSuccess) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.setSavedValue(false)
                        showSuccess = false
                        navController.navigate("savedRequests") },
                    title = { Text(stringResource(R.string.success)) },
                    text = { Text(stringResource(R.string.request_saved)) },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.setSavedValue(false)
                            showSuccess = false
                            navController.navigate("savedRequests") }) {
                            Text(stringResource(R.string.close))
                        }
                    }
                )
            }
        }
    }
}

@Preview(name = "Send Money Screen", showBackground = true)
@Composable
fun SendMoneyScreenPreview() {
    val servicesRepository = ServicesRepository( LocalContext.current)
    val requestRepository = RequestRepository(AppDatabase.getDatabase(LocalContext.current).requestDao())
    val sendMoneyViewModel: SendMoneyViewModel = viewModel(
        factory = SendMoneyViewModelFactory(
            servicesRepository,
            requestRepository
        )
    )
    SendMoneyAppTheme {
        SendMoneyScreen(sendMoneyViewModel,rememberNavController())
    }
}