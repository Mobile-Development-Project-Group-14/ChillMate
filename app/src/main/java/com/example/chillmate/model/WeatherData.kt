package com.example.chillmate.model

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather,
    val current_units: WeatherUnits,
    val daily: DailyWeather,
)

data class CurrentWeather(
    val time: String,
    val temperature_2m: Double,
    val precipitation: Double,
    val weather_code: Int,
    val wind_speed_10m: Double,
    val relative_humidity_2m: Int,
    val rain: Double,
    val cloud_cover: Int,
    val apparent_temperature: Double,
    val showers: Int,
    val is_day: Int,
    val snowfall: Double
)

data class WeatherUnits(
    val temperature_2m: String,
    val precipitation: String,
    val wind_speed_10m: String,
    val relative_humidity_2m: String
)

data class DailyWeather(
    val time: List<String>,           // Dates (e.g., ["2023-10-01", "2023-10-02"])
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val precipitation_sum: List<Double>,
    val temperature_2m: List<Double>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>
)