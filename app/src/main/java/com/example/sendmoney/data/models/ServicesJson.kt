package com.example.sendmoney.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServicesData(
    val title: Map<String, String>,
    val services: List<Service>
)

@Serializable
data class Service(
    val label: Map<String, String>? = null,
    val name: String,
    val providers: List<Provider>
)

@Serializable
data class Provider(
    val name: String,
    val id: String,
    val required_fields: List<RequiredField>
)

@Serializable
data class RequiredField(
    val label: Map<String, String>,
    val name: String,
    val placeholder: Map<String, String>? = null,
    val type: String,
    val validation: String,
    val max_length: String,
    val validation_error_message: Map<String, String>? = null,
    val options: List<Option>? = null
)

@Serializable
data class Option(
    val label: String,
    val name: String
)