package com.example.chillmate.model




//Data class for weather conditions
data class WeatherCondition(
    val location:String, //City name
    val  type: String,  //Weather condition
    val icon: String,  //Weather icon
    val temperature: Int,  //Temperature in Celsius
    val isDay: Boolean  //True if it is day time, false if it is night time
)




