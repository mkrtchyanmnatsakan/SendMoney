package com.example.sendmoney.data.repository

import android.content.Context
import com.example.sendmoney.R
import com.example.sendmoney.data.models.ServicesData
import java.io.BufferedReader
import kotlinx.serialization.json.Json

class ServicesRepository(private val context: Context) {
    fun getServices(): ServicesData {
        try {
            val jsonString = context.resources.openRawResource(R.raw.services)
                .bufferedReader()
                .use(BufferedReader::readText)
            return Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
        } catch (e: Exception) {
            return ServicesData(emptyMap(), emptyList())
        }
    }
}