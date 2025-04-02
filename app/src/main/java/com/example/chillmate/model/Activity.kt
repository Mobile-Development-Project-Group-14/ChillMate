package com.example.chillmate.model

data class Activity(
    val name: String,
    val description: String,
    val imageRes: Int,
    val category: List<String>,
    val requirements: List<String> = emptyList(),
    val nearbyPlaces: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val duration: String = "1-2 hours",
    val route: String? = null,
    val priceLevel: Int = 1,
    val difficulty: String = "Moderate",
    val bestTime: String? = null
)