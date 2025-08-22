package com.example.sendmoney.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LanguageManager {
    fun setLocale( language: String) {
        val validLanguage = when (language) {
            "en", "ar" -> language
            else -> "en"
        }

        val locale = Locale.forLanguageTag(validLanguage)
        val appLocale: LocaleListCompat = LocaleListCompat.create(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun applyLocale(context: Context, language: String) {
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration.apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        recreate(context as Activity)
    }
}