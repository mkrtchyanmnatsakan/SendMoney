package com.example.sendmoney.db

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class RequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceName: String,
    val providerName: String,
    val amount: Double,
    val jsonDetails: String
)
