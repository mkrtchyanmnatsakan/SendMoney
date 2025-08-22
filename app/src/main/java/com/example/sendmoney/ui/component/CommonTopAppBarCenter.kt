package com.example.sendmoney.ui.component

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.example.sendmoney.ui.theme.PurpleGrey40
import com.example.sendmoney.utils.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBarCenter(
    title: String,
    navController: NavController?,
    currentLocale: String,
    onNavigateUp: (() -> Unit)? = null,
    showLanguageSwitcher: Boolean = true
) {
    val context = LocalContext.current

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
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
            if (showLanguageSwitcher) {
                val (buttonText, nextLocale) = if (currentLocale == "en") {
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
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = PurpleGrey40
        )
    )
}
