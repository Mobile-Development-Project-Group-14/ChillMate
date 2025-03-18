package com.example.chillmate.model

import android.accounts.AuthenticatorDescription
import android.graphics.drawable.Icon
import android.health.connect.datatypes.units.Temperature
import com.example.chillmate.R

//Data class for weather conditions
data class WeatherCondition(
    val  type: String,
    val icon: String,
    val temperature: Int
)

//Data class for activities
data class Activity(
    val name: String,
    val description: String,
    val icon: String
)


