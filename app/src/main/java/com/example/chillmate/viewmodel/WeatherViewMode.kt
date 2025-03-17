package com.example.chillmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillmate.model.WeatherData
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    data class Success(val data: WeatherData) : WeatherUiState
    data object Error : WeatherUiState
    data object Loading : WeatherUiState
}

class WeatherViewModel : ViewModel() {
    private val defaultLocation = Pair(65.0124, 25.4682)

    var weatherUiState by mutableStateOf<WeatherUiState>(WeatherUiState.Loading)
        private set

    var currentLocation by mutableStateOf(defaultLocation)
        private set

    init {
        fetchWeatherData() // Initialize with default location
    }

    fun updateLocation(location: Pair<Double, Double>?) {
        currentLocation = location ?: defaultLocation
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        weatherUiState = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val response = WeatherApiService.instance.getWeatherData(
                    latitude = currentLocation.first,
                    longitude = currentLocation.second,
                    current = "temperature_2m,precipitation,weather_code,wind_speed_10m,relative_humidity_2m,rain"
                )
                weatherUiState = WeatherUiState.Success(response)
            } catch (e: Exception) {
                weatherUiState = WeatherUiState.Error
            }
        }
    }
}