package com.example.sendmoney.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sendmoney.R
import com.example.sendmoney.ui.theme.PurpleGrey40
import com.example.sendmoney.ui.theme.SendMoneyAppTheme
import com.example.sendmoney.utils.LanguageManager

@Composable
@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
fun LoginScreen(navController: NavController?, viewModel: LoginViewModel?) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val locale = context.resources.configuration.locales[0].language
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Clear focus if keyboard is not visible
    val isKeyboardVisible = WindowInsets.isImeVisible
    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.login),
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
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(96.dp))
            // Title
            Text(
                text = stringResource(R.string.welcome_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Subtitle
            Text(
                text = stringResource(R.string.welcome_message),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Email field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it.trim() },
                placeholder = { Text(stringResource(R.string.username)) },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel?.error?.collectAsState()?.value == true &&
                        username != "testuser",
                supportingText = {
                    if (viewModel?.error?.collectAsState()?.value == true &&
                        username != "testuser") {
                        Text(stringResource(R.string.invalid_username))
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    viewModel?.login(username, password) { navController?.navigate("sendMoney") }
                })
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it.trim() },
                placeholder = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel?.error?.collectAsState()?.value == true &&
                        password != "password123",
                supportingText = {
                    if (viewModel?.error?.collectAsState()?.value == true &&
                        password != "password123") {
                        Text(stringResource(R.string.invalid_password))
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    viewModel?.login(username, password) { navController?.navigate("sendMoney") }
                })
            )

            Spacer(modifier = Modifier.height(64.dp))
            // Sign in button
            Button(
                onClick = { viewModel?.login(username, password) { navController?.navigate("sendMoney") } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(PurpleGrey40),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(stringResource(R.string.login))
            }

            Spacer(Modifier.weight(1f))
            // Footer
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = stringResource(R.string.terms_of_service_info),
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Login Screen", showBackground = true)
@Composable
fun LoginScreenPreview() {
    SendMoneyAppTheme {
        LoginScreen(null,null)
    }
}