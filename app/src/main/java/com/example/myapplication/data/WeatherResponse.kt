package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

// Modelo principal que envuelve toda la respuesta
data class WeatherApiResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current
)

// Modelo para la información de la ubicación
data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String
)

// Modelo para los datos del clima actual
data class Current(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("condition") val condition: Condition
)

// Modelo para la descripción del clima
data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val iconUrl: String
)
