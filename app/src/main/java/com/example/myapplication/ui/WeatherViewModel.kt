package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.WeatherApiResponse
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    // **IMPORTANTE**: Pega tu clave API aquí
    private val apiKey = "3dfe419cf8a04b3a8c112502252710"

    private val _weatherData = MutableLiveData<WeatherApiResponse>()
    val weatherData: LiveData<WeatherApiResponse> get() = _weatherData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchWeatherForCity(city: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getCurrentWeather(apiKey, city)
                if (response.isSuccessful && response.body() != null) {
                    _weatherData.value = response.body()
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            }
        }
    }
}