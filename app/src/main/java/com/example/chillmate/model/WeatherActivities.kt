package com.example.chillmate.model

import android.accounts.AuthenticatorDescription
import android.graphics.drawable.Icon
import android.health.connect.datatypes.units.Temperature
import com.example.chillmate.R

//Data class for weather conditions
data class WeatherCondition(
    val location:String, //City name
    val  type: String,  //Weather condition
    val icon: String,  //Weather icon
    val temperature: Int,  //Temperature in Celsius
    val isDay: Boolean  //True if it is day time, false if it is night time
)

//Data class for activities
data class Activity(
    val name: String,
    val description: String,
    val icon: String
)


