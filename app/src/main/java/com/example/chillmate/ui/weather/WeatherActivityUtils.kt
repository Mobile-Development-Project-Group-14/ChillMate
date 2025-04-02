package com.example.chillmate.ui.weather


import com.example.chillmate.R
import com.example.chillmate.model.Activity
import com.example.chillmate.model.WeatherCondition
import com.example.chillmate.viewmodel.WeatherViewModel
import com.example.chillmate.viewmodel.WeatherUiState

// Shared function to get weather condition
fun getCurrentWeatherCondition(viewModel: WeatherViewModel): WeatherCondition {
    return viewModel.weatherUiState.let { state ->
        if (state is WeatherUiState.Success) {
            val isDay = state.data.current.is_day == 1
            val weatherType = when {
                state.data.current.precipitation > 0 -> "rain" to "🌧️"
                state.data.current.snowfall > 0 -> "snow" to "❄️"
                state.data.current.cloud_cover > 50 -> "cloudy" to "☁️"
                else -> "sunny" to if (isDay) "☀️" else "🌙"
            }
            WeatherCondition(
                location = viewModel.locationName.value,
                type = weatherType.first,
                icon = weatherType.second,
                temperature = state.data.current.temperature_2m.toInt(),
                isDay = isDay
            )
        } else {
            WeatherCondition(
                location = "Unknown",
                type = "clear",
                icon = "☀️",
                temperature = 20,
                isDay = true
            )
        }
    }
}

// getActivitySuggestions
fun getActivitySuggestions(weather: WeatherCondition) : List<Activity> {
return when {
    //Handle precipitation first
    weather.type== "rain" -> listOf(
    Activity(
        name = "Museum Marathon",
        description = "Visit 3+ museums in one day",
        imageRes = R.drawable.museum_marathon,
        category = listOf("culture", "indoor"),
        priceLevel = 2
    ),
    Activity(
        name = "Tea Ceremony Workshop",
        description = "Learn traditional brewing techniques",
        imageRes = R.drawable.tea_workshop,
        category = listOf("wellness", "educational")
    )
    )

    // ==== Freezing Conditions (<0°C) ====
    weather.temperature <= -10 -> listOf(
    Activity(
        name = "Aurora Hunting",
        description = "Chase northern lights in clear winter skies",
        imageRes = R.drawable.aurora_hunting,
        category = listOf("winter", "night"),
        requirements = listOf("Thermal suit", "Tripod", "Hot drinks")
    ),
    Activity(
        name = "Ice Sculpture Workshop",
        description = "Create art from frozen water",
        imageRes = R.drawable.ice_sculpture,
        category = listOf("creative", "winter"),
        equipment = listOf("Ice tools", "Gloves")
    )
    )

    // ==== Cold (0°C to -10°C) ====
    weather.temperature < 0 -> listOf(
    Activity(
        name = "Winter Photography",
        description = "Capture stunning frosty landscapes",
        imageRes = R.drawable.winter_photography,
        category = listOf("creative", "outdoor"),
        equipment = listOf("Camera", "Tripod")
    ),
    Activity(
        name = "Hot Spring Visit",
        description = "Relax in naturally heated mineral waters",
        imageRes = R.drawable.hot_springs,
        category = listOf("wellness", "relaxation")
    )
    )

    // ==== Chilly (0-10°C) ==== [NEW]
    weather.temperature in 0..10 -> listOf(
    Activity(
        name = "Maple Syrup Tasting",
        description = "Sample winter-harvested syrups",
        imageRes = R.drawable.syrup_tasting,
        category = listOf("food", "seasonal"),
        priceLevel = 2
    ),
    Activity(
        name = "Winter Birdwatching",
        description = "Spot migratory birds in coastal areas",
        imageRes = R.drawable.winter_birding,
        category = listOf("nature", "outdoor"),
        bestTime = "10AM-2PM"
    )
    )

    // ==== Mild (10-20°C) ==== [NEW]
    weather.temperature in 11..20 -> listOf(
    Activity(
        name = "Urban Cycling",
        description = "Explore city bike paths in cool comfort",
        imageRes = R.drawable.urban_cycling,
        category = listOf("active", "transport"),
        equipment = listOf("Bike", "Helmet")
    ),
    Activity(
        name = "Open-Air Markets",
        description = "Browse seasonal produce stalls",
        imageRes = R.drawable.spring_market,
        category = listOf("shopping", "local")
    )
    )

    // ==== Warm (20-30°C) ==== [NEW]
    weather.temperature in 21..30 -> listOf(
    Activity(
        name = "Kayak Sunrise Tour",
        description = "Paddle through calm morning waters",
        imageRes = R.drawable.kayak_sunrise,
        category = listOf("water", "sunrise"),
        requirements = listOf("Sunscreen", "Water shoes")
    ),
    Activity(
        name = "Rooftop Film Night",
        description = "Open-air cinema under the stars",
        imageRes = R.drawable.rooftop_cinema,
        category = listOf("night", "entertainment")
    )
    )

    // ==== Hot (>30°C) ==== [NEW]
    weather.temperature > 30 -> listOf(
    Activity(
        name = "Desert Stargazing",
        description = "Observe clear night skies in arid regions",
        imageRes = R.drawable.desert_stars,
        category = listOf("night", "extreme"),
        requirements = listOf("Water", "Hat")
    ),
    Activity(
        name = "Cave Exploration",
        description = "Discover cool underground formations",
        imageRes = R.drawable.cave_tour,
        category = listOf("geology", "adventure")
    )
    )

    // ==== Rainy Conditions ==== [ENHANCED]
    weather.type == "rain" -> listOf(
    Activity(
        name = "Museum Marathon",
        description = "Visit 3+ museums in one day",
        imageRes = R.drawable.museum_marathon,
        category = listOf("culture", "indoor"),
        priceLevel = 2
    ),
    Activity(
        name = "Tea Ceremony Workshop",
        description = "Learn traditional brewing techniques",
        imageRes = R.drawable.tea_workshop,
        category = listOf("wellness", "educational")
    )
    )

    // ==== Default Suggestions ==== [UPDATED]
    else -> listOf(
    Activity(
        name = "Local Café Tour",
        description = "Discover hidden gems in your city",
        imageRes = R.drawable.cafe_tour,
        category = listOf("food", "social")
    ),
    Activity(
        name = "Book Club",
        description = "Join a literary discussion group",
        imageRes = R.drawable.book_club,
        category = listOf("indoor", "social")
    )
    )
}
}

