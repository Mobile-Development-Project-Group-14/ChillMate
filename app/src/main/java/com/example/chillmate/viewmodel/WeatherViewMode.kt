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
    object Error : WeatherUiState
    object Loading : WeatherUiState
}

class WeatherViewModel : ViewModel() {
    var weatherUiState by mutableStateOf<WeatherUiState>(WeatherUiState.Loading)
        private set

    init {
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                val response = WeatherApiService.instance.getWeatherData(
                    latitude = 65.0124,
                    longitude = 25.4682,
                    current = "temperature_2m,precipitation,weather_code,wind_speed_10m,relative_humidity_2m,rain"
                )
                weatherUiState = WeatherUiState.Success(response)
            } catch (e: Exception) {
                weatherUiState = WeatherUiState.Error
            }
        }
    }
}