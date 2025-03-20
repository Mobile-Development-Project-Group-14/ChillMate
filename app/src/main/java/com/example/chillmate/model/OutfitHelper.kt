// File: OutfitHelper.kt
package com.example.chillmate.model

import com.example.chillmate.R


fun getOutfitImageForTemperature(temperature: Double): Int {
    return when {
        temperature < -6 -> R.drawable.extremecold
        temperature in -6.0..0.0 -> R.drawable.verycold
        temperature in 1.0..5.0 -> R.drawable.cold
        temperature in 6.0..10.0 -> R.drawable.chilly
        temperature in 11.0..15.0 -> R.drawable.cool
        temperature in 16.0..20.0 -> R.drawable.mild
        temperature in 21.0..25.0 -> R.drawable.warm
        temperature in 26.0..30.0 -> R.drawable.hot
        else -> R.drawable.veryhot
    }
}