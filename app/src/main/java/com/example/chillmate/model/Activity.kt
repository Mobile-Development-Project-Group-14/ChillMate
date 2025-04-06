package com.example.chillmate.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activity(
    val id: String,
    val name: String,
    val description: String,
    val detailedDescription: String,
    val imageRes: Int,
    val category: List<String>,
    val requirements: List<String> = emptyList(),
    val nearbyPlaces: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val duration: String? = null,
    val route: String? = null,
    val priceLevel: Int = 0,
    val difficulty: String = "Moderate",
    val bestTime: String? = null
) : Parcelable