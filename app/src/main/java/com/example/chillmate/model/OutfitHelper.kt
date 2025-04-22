// File: OutfitHelper.kt
package com.example.chillmate.model

import com.example.chillmate.R

enum class TemperatureRange {
    EXTREME_COLD, VERY_COLD, COLD, CHILLY, COOL, MILD, WARM, HOT, VERY_HOT
}

fun getTemperatureRange(temperature: Double): TemperatureRange {
    return when {
        temperature < -15 -> TemperatureRange.EXTREME_COLD
        temperature in -15.0..-10.0 -> TemperatureRange.VERY_COLD
        temperature in -10.0..-5.0 -> TemperatureRange.COLD
        temperature in -5.0..0.0 -> TemperatureRange.CHILLY
        temperature in 0.0..5.0 -> TemperatureRange.COOL
        temperature in 5.0..10.0 -> TemperatureRange.MILD
        temperature in 10.0..15.0 -> TemperatureRange.WARM
        temperature in 15.0..22.0 -> TemperatureRange.HOT
        temperature > 22.0 -> TemperatureRange.VERY_HOT
        else -> TemperatureRange.VERY_HOT
    }
}


fun getOutfitImageForTemperature(temperature: Double): Int {
    return when(getTemperatureRange(temperature)) {
        TemperatureRange.EXTREME_COLD -> R.drawable.extremecold
        TemperatureRange.VERY_COLD -> R.drawable.cold3
        TemperatureRange.COLD -> R.drawable.verycold
        TemperatureRange.CHILLY -> R.drawable.chilly
        TemperatureRange.COOL -> R.drawable.cool
        TemperatureRange.MILD -> R.drawable.mild
        TemperatureRange.WARM -> R.drawable.warm
        TemperatureRange.HOT -> R.drawable.hot
        TemperatureRange.VERY_HOT -> R.drawable.veryhot

    }
}