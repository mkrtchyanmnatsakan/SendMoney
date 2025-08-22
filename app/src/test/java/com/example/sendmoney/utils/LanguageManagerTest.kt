package com.example.sendmoney.utils


import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale

class LanguageManagerTest {

    @Before
    fun setUp() {
        mockkStatic(AppCompatDelegate::class)
        mockkStatic(ActivityCompat::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(AppCompatDelegate::class)
        unmockkStatic(ActivityCompat::class)
    }

    @Test
    fun `setLocale with 'en' sets English locale via AppCompatDelegate`() {
        val language = "en"
        val expectedLocale = Locale.forLanguageTag(language)
        val expectedLocaleList = LocaleListCompat.create(expectedLocale)
        val localeListSlot = slot<LocaleListCompat>()

        every { AppCompatDelegate.setApplicationLocales(capture(localeListSlot)) } just Runs

        LanguageManager.setLocale(language)

        verify(exactly = 1) { AppCompatDelegate.setApplicationLocales(any()) }
        assertThat(localeListSlot.captured.toLanguageTags()).isEqualTo(expectedLocaleList.toLanguageTags())
        assertThat(localeListSlot.captured[0]).isEqualTo(expectedLocale)
    }

    @Test
    fun `setLocale with 'ar' sets Arabic locale via AppCompatDelegate`() {
        val language = "ar"
        val expectedLocale = Locale.forLanguageTag(language)
        val expectedLocaleList = LocaleListCompat.create(expectedLocale)
        val localeListSlot = slot<LocaleListCompat>()

        every { AppCompatDelegate.setApplicationLocales(capture(localeListSlot)) } just Runs

        LanguageManager.setLocale(language)

        verify(exactly = 1) { AppCompatDelegate.setApplicationLocales(any()) }
        assertThat(localeListSlot.captured.toLanguageTags()).isEqualTo(expectedLocaleList.toLanguageTags())
        assertThat(localeListSlot.captured[0]).isEqualTo(expectedLocale)
    }

    @Test
    fun `setLocale with invalid language defaults to English`() {
        val language = "fr"
        val expectedDefaultLocale = Locale.forLanguageTag("en")
        val expectedLocaleList = LocaleListCompat.create(expectedDefaultLocale)
        val localeListSlot = slot<LocaleListCompat>()

        every { AppCompatDelegate.setApplicationLocales(capture(localeListSlot)) } just Runs

        LanguageManager.setLocale(language)

        verify(exactly = 1) { AppCompatDelegate.setApplicationLocales(any()) }
        assertThat(localeListSlot.captured.toLanguageTags()).isEqualTo(expectedLocaleList.toLanguageTags())
        assertThat(localeListSlot.captured[0]).isEqualTo(expectedDefaultLocale)
    }

    @Test
    fun `applyLocale applies configuration and recreates activity`() {
        val mockContext = mockk<Context>(relaxed = true)
        val mockActivity = mockk<Activity>(relaxed = true) // Use relaxed for Activity context
        val mockResources = mockk<Resources>(relaxed = true)
        val mockConfiguration = mockk<Configuration>(relaxed = true)
        val mockDisplayMetrics = mockk<DisplayMetrics>(relaxed = true)

        val language = "ar"
        val expectedLocale = Locale.forLanguageTag(language)
        val currentDefaultLocale = Locale.getDefault() // Save current for restoration

        // Stubbing context and resources chain
        every { mockContext.resources } returns mockResources
        every { mockResources.configuration } returns mockConfiguration
        every { mockResources.displayMetrics } returns mockDisplayMetrics

        every { ActivityCompat.recreate(mockActivity) } just Runs

        val contextAsActivity: Context = mockActivity
        every { contextAsActivity.resources } returns mockResources

        LanguageManager.applyLocale(contextAsActivity, language)

        assertThat(Locale.getDefault()).isEqualTo(expectedLocale)

        verify { mockConfiguration.setLocale(expectedLocale) }
        verify { mockConfiguration.setLayoutDirection(expectedLocale) }
        verify { mockResources.updateConfiguration(mockConfiguration, mockDisplayMetrics) }
        verify { ActivityCompat.recreate(mockActivity) }

        Locale.setDefault(currentDefaultLocale)
    }
}
