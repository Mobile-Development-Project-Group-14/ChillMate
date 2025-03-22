// File: OutfitHelper.kt
package com.example.chillmate.model

import com.example.chillmate.R


fun getOutfitImageForTemperature(temperature: Double): Int {
    return when {
        temperature < -20 -> R.drawable.extremecold
        temperature in -20.0..-10.0 -> R.drawable.cold
        temperature in -10.0..0.0 -> R.drawable.verycold
        temperature in 0.0..10.0 -> R.drawable.chilly
        temperature in 10.0..15.0 -> R.drawable.cool
        temperature in 15.0..20.0 -> R.drawable.mild
        temperature in 20.0..25.0 -> R.drawable.warm
        temperature in 25.0..30.0 -> R.drawable.hot
        else -> R.drawable.veryhot
    }
}