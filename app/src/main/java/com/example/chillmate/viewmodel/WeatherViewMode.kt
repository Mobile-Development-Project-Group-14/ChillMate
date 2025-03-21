package com.example.chillmate.viewmodel

import WeatherApiService
import android.app.Application
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillmate.R
import com.example.chillmate.model.WeatherData
import com.example.chillmate.model.getOutfitImageForTemperature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface WeatherUiState {
    data class Success(val data: WeatherData) : WeatherUiState
    data object Error : WeatherUiState
    data object Loading : WeatherUiState
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val defaultLocation = Pair(65.0124,25.4682) // Default location

    // State for location name
    private val _locationName = MutableStateFlow("")
    val locationName: StateFlow<String> = _locationName

    // Call this when coordinates are updated (e.g., from API)
    fun updateLocationName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _locationName.value = reverseGeocode(latitude, longitude)
        }
    }

    // Reverse geocoding using Geocoder
    @Suppress("DEPRECATION")
    private fun reverseGeocode(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(getApplication<Application>().applicationContext)
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.let { address ->
                address.locality ?: address.subAdminArea ?: address.adminArea ?: "$latitude, $longitude"
            } ?: "$latitude, $longitude" // Fallback to coordinates
        } catch (e: IOException) {
            "$latitude, $longitude" // Fallback if geocoding fails
        }
    }

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