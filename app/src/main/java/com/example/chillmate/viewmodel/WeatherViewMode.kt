package com.example.chillmate.viewmodel

import WeatherApiService
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillmate.R
import com.example.chillmate.model.WeatherData
import com.example.chillmate.model.getOutfitImageForTemperature
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
    var currentOutfitImage by mutableIntStateOf(R.drawable.mild) // Default image
        private set

    private fun updateOutfitImage(temperature: Double) {
        currentOutfitImage = getOutfitImageForTemperature(temperature)
    }

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
                    current = "temperature_2m,precipitation,weather_code,relative_humidity_2m,rain,cloud_cover,apparent_temperature,showers,is_day,snowfall"
                )
                weatherUiState = WeatherUiState.Success(response)
                updateOutfitImage(response.current.temperature_2m)
            } catch (e: Exception) {
                weatherUiState = WeatherUiState.Error
            }
        }
    }
}