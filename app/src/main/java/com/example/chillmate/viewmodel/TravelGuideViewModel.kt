package com.example.chillmate.viewmodel

import WeatherApiService
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillmate.model.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

class TravelGuideViewModel : ViewModel() {
    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    private val _selectedCity = MutableStateFlow<City?>(null)
    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData: StateFlow<WeatherData?> = _weatherData.asStateFlow()

    private val _selectedDates = MutableStateFlow<Pair<LocalDate, LocalDate>?>(null)
    val selectedDates: StateFlow<Pair<LocalDate, LocalDate>?> = _selectedDates.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val maxFutureDays = 16

    private val geoNamesService = Retrofit.Builder()
        .baseUrl("http://geodb-free-service.wirefreethought.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoNamesService::class.java)

    fun selectCity(city: City) {
        _selectedCity.value = city
        _cities.value = emptyList()
        _errorMessage.value = null
    }


    fun clearState() {
        _selectedCity.value = null
        _selectedDates.value = null
        _weatherData.value = null
        _errorMessage.value = null
        _cities.value = emptyList()
    }

    fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                if (query.length < 3) {
                    _cities.value = emptyList()
                    return@launch
                }

                val response = geoNamesService.searchCities(
                    namePrefix = query,
                    limit = 10
                )

                _cities.value = response.data.map {
                    City(it.name, it.country, it.latitude, it.longitude)
                }
                _errorMessage.value = null
            } catch (e: Exception) {
                _cities.value = emptyList()
                _errorMessage.value = "Failed to search cities: ${e.message}"
            }
        }
    }

    fun setDates(start: LocalDate, end: LocalDate) {
        _selectedDates.value = Pair(start, end)
        _errorMessage.value = null // Clear previous errors when selecting new dates
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateTravelGuide() {
        val city = _selectedCity.value
        val dates = _selectedDates.value

        when {
            city == null -> _errorMessage.value = "Please select a destination city"
            dates == null -> _errorMessage.value = "Please select travel dates"
            !isDateRangeValid(dates.first, dates.second) -> {
                val today = LocalDate.now()
                val maxDate = today.plusDays(maxFutureDays.toLong())
                _errorMessage.value = "Dates must be between $today and $maxDate"
            }
            else -> {
                _errorMessage.value = null
                viewModelScope.launch {
                    try {
                        val today = LocalDate.now()
                        val maxAllowedDate = today.plusDays(maxFutureDays.toLong())
                        if (dates.second == maxAllowedDate) {
                            // Handle edge case for maximum date
                            _errorMessage.value = "Fetching weather data for maximum date range..."
                        }
                        val response = WeatherApiService.instance.getWeatherData(
                            latitude = city.lat,
                            longitude = city.lon,
                            current = "temperature_2m,precipitation,weather_code",
                            daily = "weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,uv_index_max",
                            startDate = dates.first.toString(),
                            endDate = dates.second.toString()
                        )
                        // Additional validation of the response
                        if (response.daily.time.isEmpty()) {
                            throw Exception("No weather data available for selected dates")
                        }
                        _weatherData.value = response
                    } catch (e: Exception) {
                        _weatherData.value = null
                        _errorMessage.value = when {
                            e.message?.contains("404") == true -> "Weather data not available for selected location/dates"
                            e.message?.contains("400") == true -> "Invalid date range requested"
                            else -> "Failed to get weather data: ${e.message ?: "Unknown error"}"
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isDateRangeValid(start: LocalDate, end: LocalDate): Boolean {
        val today = LocalDate.now()
        val maxAllowedDate = today.plusDays(maxFutureDays.toLong())
        return !start.isBefore(today) &&
                !end.isAfter(maxAllowedDate) &&  // Changed from isAfter to !isAfter
                !start.isAfter(end)
    }
}

// Data classes remain the same but should match the GeoNames API response structure
data class City(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

interface GeoNamesService {
    @GET("v1/geo/cities")
    suspend fun searchCities(
        @Query("namePrefix") namePrefix: String,
        @Query("limit") limit: Int = 10
    ): GeoNamesResponse
}

data class GeoNamesResponse(
    val data: List<GeoCity>
)

data class GeoCity(
    val name: String,    // Changed from 'city' to match actual API response
    val country: String,
    val latitude: Double,
    val longitude: Double
)