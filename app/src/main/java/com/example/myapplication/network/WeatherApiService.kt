package com.example.myapplication.network

import com.example.myapplication.data.WeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current.json") // Este es el endpoint para el clima actual
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String, // Parámetro para tu clave API
        @Query("q") city: String,    // Parámetro para la ciudad
        @Query("aqi") aqi: String = "no", // Parámetro adicional (opcional)
        @Query("lang") lang: String = "es" // Para obtener la descripción en español
    ): Response<WeatherApiResponse>
}