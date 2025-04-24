package com.example.chillmate.viewmodel

import WeatherApiService
import android.app.Application
import android.location.Geocoder
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillmate.R
import com.example.chillmate.model.WeatherData
import com.example.chillmate.model.getOutfitImageForTemperature
import com.example.chillmate.model.getTemperatureRange
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.math.abs

sealed interface WeatherUiState {
    data class Success(val data: WeatherData) : WeatherUiState
    data object Error : WeatherUiState
    data object Loading : WeatherUiState
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val defaultLocation = Pair(65.0124,25.4682) // Default location

    // Location state
    private val _locationName = MutableStateFlow("")
    val locationName: StateFlow<String> = _locationName

    // Weather state
    var weatherUiState by mutableStateOf<WeatherUiState>(WeatherUiState.Loading)
        private set

    // UI state
    private var currentLocation by mutableStateOf(defaultLocation)
    var currentOutfitImage by mutableIntStateOf(R.drawable.mild)
        private set

    init {
        fetchWeatherData()
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
                    current = "temperature_2m,precipitation,weather_code,relative_humidity_2m,rain,cloud_cover,apparent_temperature,showers,is_day,snowfall",
                    daily = "temperature_2m_max,temperature_2m_min,precipitation_sum,uv_index_max",
                    forecast_days = 7
                )
                weatherUiState = WeatherUiState.Success(response)
                updateOutfitImage(response.current.temperature_2m)
                updateLocationName(currentLocation.first, currentLocation.second)

                // Check for alerts after 5 seconds
                delay(5000)
                checkForAlerts(response)
            } catch (e: Exception) {
                weatherUiState = WeatherUiState.Error
            }
        }
    }
    private val _activeAlerts = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val activeAlerts: StateFlow<List<WeatherAlert>> = _activeAlerts

    private suspend fun checkForAlerts(weatherData: WeatherData) {
        val alerts = mutableListOf<WeatherAlert>()
        val daily = weatherData.daily


        // Temperature difference alerts
        if (daily.time.size > 1 && daily.temperature_2m_max.size > 1) {
            val todayMax = daily.temperature_2m_max[0]
            val tomorrowMax = daily.temperature_2m_max[1]
            val tempDifference = tomorrowMax - todayMax
            val absoluteDiff = abs(tempDifference)

            when {
                absoluteDiff >= 10 -> alerts.add(
                    WeatherAlert(
                        "Major Temperature Change",
                        "Severe: ${"%.1f".format(absoluteDiff)}°C ${if(tempDifference > 0) "warmer" else "cooler"} tomorrow",
                        AlertSeverity.SEVERE
                    )
                )
                absoluteDiff >= 5 -> alerts.add(
                    WeatherAlert(
                        "Significant Temperature Change",
                        "Moderate: ${"%.1f".format(absoluteDiff)}°C ${if(tempDifference > 0) "warmer" else "cooler"} tomorrow",
                        AlertSeverity.MODERATE
                    )
                )
                absoluteDiff >= 2 -> alerts.add(
                    WeatherAlert(
                        "Slight Temperature Change",
                        "Minor: ${"%.1f".format(absoluteDiff)}°C ${if(tempDifference > 0) "warmer" else "cooler"} tomorrow",
                        AlertSeverity.MINOR
                    )
                )
            }
        }

        // Precipitation alerts (existing code)
        if (daily.precipitation_sum.size > 1) {
            val tomorrowPrecipice = daily.precipitation_sum[1]
            val isSnow = weatherData.current.weather_code in listOf(71, 73, 75, 77, 85, 86)

            when {
                tomorrowPrecipice >= 15 -> alerts.add(
                    WeatherAlert(
                        if (isSnow) "Heavy Snow Alert" else "Heavy Rain Alert",
                        "${"%.1f".format(tomorrowPrecipice)}mm ${if (isSnow) "snow" else "rain"} expected tomorrow",
                        AlertSeverity.SEVERE
                    )
                )
                tomorrowPrecipice >= 5 -> alerts.add(
                    WeatherAlert(
                        if (isSnow) "Snow Alert" else "Rain Alert",
                        "${"%.1f".format(tomorrowPrecipice)}mm ${if (isSnow) "snow" else "rain"} expected tomorrow",
                        AlertSeverity.MODERATE
                    )
                )
            }
        }

        _activeAlerts.value = alerts.sortedByDescending { it.severity.ordinal }

        delay(5000)
        _activeAlerts.value = emptyList()
    }

    fun dismissAlerts() {
        _activeAlerts.value = emptyList()
    }
    internal var currentTemperature by mutableStateOf(0.0)

    private fun updateOutfitImage(temperature: Double) {
        currentTemperature = temperature
        val range = getTemperatureRange(temperature)
        currentOutfitImage = getOutfitImageForTemperature(temperature)
    }

    private fun updateLocationName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _locationName.value = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val geocoder = Geocoder(getApplication<Application>().applicationContext)
                    val listener = object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<android.location.Address>) {
                            _locationName.value = addresses.firstOrNull()?.let { address ->
                                address.locality ?: address.subAdminArea ?: address.adminArea ?: "$latitude, $longitude"
                            } ?: "$latitude, $longitude"
                        }

                        override fun onError(errorMessage: String?) {
                            _locationName.value = "$latitude, $longitude"
                        }
                    }
                    geocoder.getFromLocation(latitude, longitude, 1, listener)
                    "$latitude, $longitude" // Temporary value until callback completes
                } else {
                    // Fallback for older versions
                    val geocoder = Geocoder(getApplication<Application>().applicationContext)
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    addresses?.firstOrNull()?.let { address ->
                        address.locality ?: address.subAdminArea ?: address.adminArea ?: "$latitude, $longitude"
                    } ?: "$latitude, $longitude"
                }
            } catch (e: IOException) {
                "$latitude, $longitude"
            }
        }
    }
}

data class WeatherAlert(
    val title: String,
    val description: String,
    val severity: AlertSeverity
)

enum class AlertSeverity { MINOR, MODERATE, SEVERE }