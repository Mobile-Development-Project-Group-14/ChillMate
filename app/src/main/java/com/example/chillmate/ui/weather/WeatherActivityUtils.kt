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

fun getActivitySuggestions(weather: WeatherCondition): List<Activity> {
    return when {
        // ==== Rainy Conditions ====
        weather.type == "rain" -> listOf(
            Activity(
                name = "Museum Marathon",
                description = "Visit 3+ museums in one day with a cultural pass",
                imageRes = R.drawable.museum_marathon,
                category = listOf("culture", "indoor"),
                priceLevel = 2
            ),
            Activity(
                name = "Tea Ceremony Workshop",
                description = "Learn traditional brewing techniques from experts",
                imageRes = R.drawable.tea_workshop,
                category = listOf("wellness", "educational")
            ),
            Activity(
                name = "Indoor Rock Climbing",
                description = "Challenge yourself at a local climbing gym",
                imageRes = R.drawable.indoor_climbing,
                category = listOf("active", "indoor"),
                equipment = listOf("Climbing shoes", "Comfortable clothes")
            ),
            Activity(
                name = "Bookstore Crawl",
                description = "Explore independent bookstores with cozy cafes",
                imageRes = R.drawable.bookstore_crawl,
                category = listOf("leisure", "indoor")
            )
        )

        // ==== Freezing Conditions (<-10°C) ====
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
                description = "Create art from frozen water with professional tools",
                imageRes = R.drawable.ice_sculpture,
                category = listOf("creative", "winter"),
                equipment = listOf("Ice tools", "Gloves")
            ),
            Activity(
                name = "Winter Sauna Experience",
                description = "Traditional sauna with ice swimming",
                imageRes = R.drawable.winter_sauna,
                category = listOf("wellness", "winter"),
                requirements = listOf("Swimsuit", "Towel")
            ),
            Activity(
                name = "Frozen Lake Walk",
                description = "Guided walk on certified safe frozen lakes",
                imageRes = R.drawable.frozen_lake,
                category = listOf("nature", "winter"),
                equipment = listOf("Ice grips", "Warm boots")
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
            ),
            Activity(
                name = "Winter Market Exploration",
                description = "Visit seasonal markets with local crafts",
                imageRes = R.drawable.winter_market,
                category = listOf("shopping", "seasonal")
            ),
            Activity(
                name = "Indoor Botanical Garden",
                description = "Explore tropical plants in warm conservatories",
                imageRes = R.drawable.botanical_garden,
                category = listOf("nature", "indoor")
            )
        )

        // ==== Chilly (0-10°C) ====
        weather.temperature in 0..10 -> listOf(
            Activity(
                name = "Maple Syrup Tasting",
                description = "Sample winter-harvested syrups at local farms",
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
            ),
            Activity(
                name = "Historical Walking Tour",
                description = "Discover city history with knowledgeable guides",
                imageRes = R.drawable.history_tour,
                category = listOf("educational", "outdoor")
            ),
            Activity(
                name = "Antique Shop Exploration",
                description = "Browse vintage items in cozy shops",
                imageRes = R.drawable.antique_shopping,
                category = listOf("shopping", "indoor")
            )
        )

        // ==== Mild (10-20°C) ====
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
            ),
            Activity(
                name = "Picnic in the Park",
                description = "Enjoy outdoor dining in scenic locations",
                imageRes = R.drawable.park_picnic,
                category = listOf("food", "outdoor")
            ),
            Activity(
                name = "Street Art Tour",
                description = "Discover urban murals and installations",
                imageRes = R.drawable.street_art,
                category = listOf("art", "outdoor")
            )
        )

        // ==== Warm (20-30°C) ====
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
            ),
            Activity(
                name = "Outdoor Yoga Session",
                description = "Morning yoga in scenic locations",
                imageRes = R.drawable.outdoor_yoga,
                category = listOf("wellness", "outdoor")
            ),
            Activity(
                name = "Farmers Market Visit",
                description = "Fresh local produce and artisanal goods",
                imageRes = R.drawable.farmers_market,
                category = listOf("food", "local")
            )
        )

        // ==== Hot (>30°C) ====
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
            ),
            Activity(
                name = "Waterpark Day",
                description = "Beat the heat with thrilling water slides",
                imageRes = R.drawable.waterpark,
                category = listOf("water", "family")
            ),
            Activity(
                name = "Indoor Ice Skating",
                description = "Cool off while skating in air-conditioned rinks",
                imageRes = R.drawable.ice_skating,
                category = listOf("sports", "indoor")
            )
        )

        // ==== Default Suggestions ====
        else -> listOf(
            Activity(
                name = "Local Café Tour",
                description = "Discover hidden gems in your city",
                imageRes = R.drawable.cafe_tour,
                category = listOf("food", "social")
            ),
            Activity(
                name = "Book Club Meeting",
                description = "Join a literary discussion group",
                imageRes = R.drawable.book_club,
                category = listOf("indoor", "social")
            ),
            Activity(
                name = "Art Gallery Visit",
                description = "Explore current exhibitions",
                imageRes = R.drawable.art_gallery,
                category = listOf("culture", "indoor")
            ),
            Activity(
                name = "Neighborhood Walk",
                description = "Discover local architecture and history",
                imageRes = R.drawable.neighborhood_walk,
                category = listOf("outdoor", "educational")
            )
        )
    }
}

