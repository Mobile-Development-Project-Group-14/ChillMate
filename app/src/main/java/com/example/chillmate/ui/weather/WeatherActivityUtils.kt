package com.example.chillmate.ui.weather

import com.example.chillmate.R
import com.example.chillmate.model.Activity
import com.example.chillmate.model.WeatherCondition
import com.example.chillmate.viewmodel.WeatherViewModel
import com.example.chillmate.viewmodel.WeatherUiState

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

fun getActivitySuggestions(weather: WeatherCondition): List<Activity> {
    return when {
        // ==== Rainy Conditions ====
        weather.type == "rain" -> listOf(
            Activity(
                id = "museum_marathon",
                name = "Museum Marathon",
                description = "Visit 3+ museums in one day with a cultural pass",
                detailedDescription = "Explore the city's top museums with an all-access pass. Includes:\n\n• Guided tours at each location\n• Special exhibits access\n• Discounts at museum cafes\n• Perfect for art and history lovers",
                imageRes = R.drawable.museum_marathon,
                category = listOf("culture", "indoor"),
                priceLevel = 2,
                duration = "Full day",
                equipment = listOf("Comfortable shoes", "Camera", "Museum map"),
                requirements = listOf("Passport for verification", "Pre-booking recommended")
            ),
            Activity(
                id = "tea_workshop",
                name = "Tea Ceremony Workshop",
                description = "Learn traditional brewing techniques from experts",
                detailedDescription = "Immerse yourself in ancient tea traditions with:\n\n• Hands-on brewing sessions\n• Rare tea tastings\n• Ceremony etiquette lessons\n• Take-home starter kit",
                imageRes = R.drawable.tea_workshop,
                category = listOf("wellness", "educational"),
                priceLevel = 1,
                duration = "2 hours",
                bestTime = "Afternoons"
            ),
            Activity(
                id = "indoor_climbing",
                name = "Indoor Rock Climbing",
                description = "Challenge yourself at a local climbing gym",
                detailedDescription = "Full-service climbing facility featuring:\n\n• 30+ climbing routes (various difficulties)\n• Bouldering area\n• Equipment rental available\n• Certified instructors on-site",
                imageRes = R.drawable.indoor_climbing,
                category = listOf("active", "indoor"),
                priceLevel = 2,
                duration = "1-3 hours",
                equipment = listOf("Climbing shoes", "Comfortable clothes"),
                requirements = listOf("Safety briefing required", "Minimum age 8")
            ),
            Activity(
                id = "bookstore_crawl",
                name = "Bookstore Crawl",
                description = "Explore independent bookstores with cozy cafes",
                detailedDescription = "Self-guided tour of the city's best indie bookshops including:\n\n• Exclusive signed editions\n• Rare book collections\n• Cozy reading nooks\n• Specialty book-themed drinks",
                imageRes = R.drawable.bookstore_crawl,
                category = listOf("leisure", "indoor"),
                priceLevel = 1,
                duration = "Flexible",
                bestTime = "Weekend afternoons"
            )
        )

        // ==== Freezing Conditions (<-10°C) ====
        weather.temperature <= -10 -> listOf(
            Activity(
                id = "winter_sauna",
                name = "Winter Sauna Experience",
                description = "Traditional sauna with ice swimming",
                detailedDescription = "Authentic Finnish lakeside sauna experience featuring:\n\n• Private 2-hour sauna session\n• Ice swimming hole access\n• Herbal tea and light snacks\n• Changing rooms with towels provided\n• Lakeside relaxation area",
                imageRes = R.drawable.winter_sauna,
                category = listOf("wellness", "winter"),
                priceLevel = 2,
                duration = "2-3 hours",
                equipment = listOf("Swimsuit", "Towel", "Water bottle"),
                requirements = listOf("Minimum age 12", "Basic swimming skills"),
                bestTime = "Evenings (16:00-20:00)"
            ),
            Activity(
                id = "aurora_hunting",
                name = "Aurora Hunting",
                description = "Chase northern lights in clear winter skies",
                detailedDescription = "Guided aurora viewing tour includes:\n\n• Transportation to prime locations\n• Professional photographer guide\n• Thermal suits and boots rental\n• Hot drinks and snacks\n• Photography tips and assistance",
                imageRes = R.drawable.aurora_hunting,
                category = listOf("winter", "night"),
                priceLevel = 3,
                duration = "4-5 hours",
                equipment = listOf("Camera", "Tripod", "Extra batteries"),
                requirements = listOf("Warm base layers", "Reservation required"),
                bestTime = "20:00-02:00"
            ),
            Activity(
                id = "ice_sculpture",
                name = "Ice Sculpture Workshop",
                description = "Create art from frozen water with professional tools",
                detailedDescription = "Hands-on ice carving experience with:\n\n• Professional ice carving tools\n• Expert instruction\n• Individual workstations\n• Photoshoot of your creation\n• Hot drinks provided",
                imageRes = R.drawable.ice_sculpture,
                category = listOf("creative", "winter"),
                priceLevel = 2,
                duration = "3 hours",
                equipment = listOf("Ice tools", "Gloves", "Warm boots"),
                requirements = listOf("Minimum age 16", "Pre-registration")
            ),
            Activity(
                id = "frozen_lake",
                name = "Frozen Lake Walk",
                description = "Guided walk on certified safe frozen lakes",
                detailedDescription = "Certified ice walking adventure including:\n\n• Safety briefing and equipment\n• Local guide with ice knowledge\n• Hot drinks by campfire\n• Ice thickness demonstration\n• Winter safety tips",
                imageRes = R.drawable.frozen_lake,
                category = listOf("nature", "winter"),
                priceLevel = 1,
                duration = "1.5 hours",
                equipment = listOf("Ice grips", "Warm boots", "Thermal layers"),
                requirements = listOf("Minimum age 10", "Signed waiver")
            )
        )

        // ==== Cold (0°C to -10°C) ====
        weather.temperature < 0 -> listOf(
            Activity(
                id = "winter_photography",
                name = "Winter Photography",
                description = "Capture stunning frosty landscapes",
                detailedDescription = "Outdoor photography workshop covering:\n\n• Winter lighting techniques\n• Camera settings for snow\n• Composition in white landscapes\n• Equipment care in cold\n• Post-processing tips",
                imageRes = R.drawable.winter_photography,
                category = listOf("creative", "outdoor"),
                priceLevel = 1,
                duration = "2.5 hours",
                equipment = listOf("Camera", "Tripod", "Lens cloth"),
                requirements = listOf("Basic camera knowledge"),
                bestTime = "Golden hour"
            ),
            Activity(
                id = "hot_springs",
                name = "Hot Spring Visit",
                description = "Relax in naturally heated mineral waters",
                detailedDescription = "Open-air geothermal springs featuring:\n\n• Multiple temperature pools\n• Mineral-rich waters\n• Mountain views\n• Changing facilities\n• Massage services available",
                imageRes = R.drawable.hot_springs,
                category = listOf("wellness", "relaxation"),
                priceLevel = 1,
                duration = "2 hours",
                equipment = listOf("Swimsuit", "Flip flops", "Water bottle"),
                bestTime = "Morning or evening"
            ),
            Activity(
                id = "winter_market",
                name = "Winter Market Exploration",
                description = "Visit seasonal markets with local crafts",
                detailedDescription = "Festive winter market highlights:\n\n• Handmade local crafts\n• Seasonal foods and drinks\n• Live demonstrations\n• Carol singers\n• Holiday decorations",
                imageRes = R.drawable.winter_market,
                category = listOf("shopping", "seasonal"),
                priceLevel = 0,
                duration = "1-2 hours",
                bestTime = "Weekends 10AM-6PM"
            ),
            Activity(
                id = "botanical_garden",
                name = "Indoor Botanical Garden",
                description = "Explore tropical plants in warm conservatories",
                detailedDescription = "Year-round tropical oasis featuring:\n\n• Rare plant collections\n• Themed greenhouse sections\n• Guided tours\n• Butterfly garden\n• Educational exhibits",
                imageRes = R.drawable.botanical_garden,
                category = listOf("nature", "indoor"),
                priceLevel = 1,
                duration = "1.5 hours",
                bestTime = "Weekday mornings"
            )
        )

        // ==== Chilly (0-10°C) ====
        weather.temperature in 0..10 -> listOf(
            Activity(
                id = "syrup_tasting",
                name = "Maple Syrup Tasting",
                description = "Sample winter-harvested syrups at local farms",
                detailedDescription = "Maple syrup experience including:\n\n• Farm tour\n• Tapping demonstration\n• Grading comparison\n• Cooking demonstration\n• Products for purchase",
                imageRes = R.drawable.syrup_tasting,
                category = listOf("food", "seasonal"),
                priceLevel = 2,
                duration = "1.5 hours",
                requirements = listOf("Reservation required"),
                bestTime = "10AM-2PM"
            ),
            Activity(
                id = "winter_birding",
                name = "Winter Birdwatching",
                description = "Spot migratory birds in coastal areas",
                detailedDescription = "Guided birdwatching tour features:\n\n• Expert ornithologist guide\n• High-quality spotting scopes\n• Species checklist\n• Hot drinks provided\n• Conservation information",
                imageRes = R.drawable.winter_birding,
                category = listOf("nature", "outdoor"),
                priceLevel = 1,
                duration = "3 hours",
                equipment = listOf("Binoculars", "Field guide", "Warm layers"),
                bestTime = "10AM-2PM"
            ),
            Activity(
                id = "history_tour",
                name = "Historical Walking Tour",
                description = "Discover city history with knowledgeable guides",
                detailedDescription = "Engaging city history walk covering:\n\n• Founding stories\n• Architectural highlights\n• Famous residents\n• Hidden gems\n• Photo opportunities",
                imageRes = R.drawable.history_tour,
                category = listOf("educational", "outdoor"),
                priceLevel = 1,
                duration = "2 hours",
                equipment = listOf("Comfortable shoes", "Weather-appropriate clothing"),
                bestTime = "Weekend afternoons"
            ),
            Activity(
                id = "antique_shopping",
                name = "Antique Shop Exploration",
                description = "Browse vintage items in cozy shops",
                detailedDescription = "Self-guided antique district tour including:\n\n• Curated shop map\n• Specialty dealers\n• Restoration experts\n• Coffee stops\n• Bargaining tips",
                imageRes = R.drawable.antique_shopping,
                category = listOf("shopping", "indoor"),
                priceLevel = 2,
                duration = "Flexible",
                bestTime = "Weekdays"
            )
        )

        // ==== Mild (10-20°C) ====
        weather.temperature in 11..20 -> listOf(
            Activity(
                id = "urban_cycling",
                name = "Urban Cycling",
                description = "Explore city bike paths in cool comfort",
                detailedDescription = "Guided bike tour highlights:\n\n• Safety briefing\n• Quality bike rental\n• Themed routes (art, history, food)\n• Photo stops\n• Local insights",
                imageRes = R.drawable.urban_cycling,
                category = listOf("active", "transport"),
                priceLevel = 1,
                duration = "2-4 hours",
                equipment = listOf("Bike", "Helmet", "Water bottle"),
                requirements = listOf("Basic cycling skills")
            ),
            Activity(
                id = "spring_market",
                name = "Open-Air Markets",
                description = "Browse seasonal produce stalls",
                detailedDescription = "Vibrant outdoor market featuring:\n\n• Local farmers\n• Artisan food producers\n• Live music\n• Cooking demonstrations\n• Family activities",
                imageRes = R.drawable.spring_market,
                category = listOf("shopping", "local"),
                priceLevel = 0,
                duration = "1-2 hours",
                bestTime = "Weekend mornings"
            ),
            Activity(
                id = "park_picnic",
                name = "Picnic in the Park",
                description = "Enjoy outdoor dining in scenic locations",
                detailedDescription = "Curated picnic experience including:\n\n• Local specialty basket\n• Blanket setup\n• Recommended spots\n• Games provided\n• Cleanup service",
                imageRes = R.drawable.park_picnic,
                category = listOf("food", "outdoor"),
                priceLevel = 1,
                duration = "1-3 hours",
                equipment = listOf("Picnic blanket", "Sunscreen")
            ),
            Activity(
                id = "street_art",
                name = "Street Art Tour",
                description = "Discover urban murals and installations",
                detailedDescription = "Guided street art exploration covering:\n\n• Artist backgrounds\n• Technique explanations\n• Photo opportunities\n• Emerging artists\n• Neighborhood history",
                imageRes = R.drawable.street_art,
                category = listOf("art", "outdoor"),
                priceLevel = 0,
                duration = "1.5 hours",
                bestTime = "Daylight hours"
            )
        )

        // ==== Warm (20-30°C) ====
        weather.temperature in 21..30 -> listOf(
            Activity(
                id = "kayak_sunrise",
                name = "Kayak Sunrise Tour",
                description = "Paddle through calm morning waters",
                detailedDescription = "Early morning kayak adventure featuring:\n\n• Stable tandem kayaks\n• Safety briefing\n• Guided route\n• Wildlife spotting\n• Sunrise photography",
                imageRes = R.drawable.kayak_sunrise,
                category = listOf("water", "sunrise"),
                priceLevel = 2,
                duration = "2.5 hours",
                equipment = listOf("Sunscreen", "Water shoes", "Hat"),
                requirements = listOf("Basic swimming ability")
            ),
            Activity(
                id = "rooftop_cinema",
                name = "Rooftop Film Night",
                description = "Open-air cinema under the stars",
                detailedDescription = "Premium outdoor movie experience with:\n\n• Comfortable seating\n• Blankets provided\n• Gourmet snacks\n• Skyline views\n• Curated film selection",
                imageRes = R.drawable.rooftop_cinema,
                category = listOf("night", "entertainment"),
                priceLevel = 2,
                duration = "3-4 hours",
                bestTime = "Summer evenings"
            ),
            Activity(
                id = "outdoor_yoga",
                name = "Outdoor Yoga Session",
                description = "Morning yoga in scenic locations",
                detailedDescription = "All-levels yoga classes featuring:\n\n• Professional instructors\n• Mats provided\n• Breathtaking views\n• Meditation component\n• Small class sizes",
                imageRes = R.drawable.outdoor_yoga,
                category = listOf("wellness", "outdoor"),
                priceLevel = 1,
                duration = "1 hour",
                equipment = listOf("Comfortable clothes", "Water bottle"),
                bestTime = "Morning hours"
            ),
            Activity(
                id = "farmers_market",
                name = "Farmers Market Visit",
                description = "Fresh local produce and artisanal goods",
                detailedDescription = "Authentic farmers market experience with:\n\n• Seasonal produce\n• Local specialties\n• Producer meet-and-greet\n• Cooking samples\n• Live demonstrations",
                imageRes = R.drawable.farmers_market,
                category = listOf("food", "local"),
                priceLevel = 0,
                duration = "1-2 hours",
                bestTime = "Weekend mornings"
            )
        )

        // ==== Hot (>30°C) ====
        weather.temperature > 30 -> listOf(
            Activity(
                id = "desert_stars",
                name = "Desert Stargazing",
                description = "Observe clear night skies in arid regions",
                detailedDescription = "Premium astronomy experience including:\n\n• High-powered telescopes\n• Expert guides\n• Constellation charts\n• Comfortable viewing area\n• Night photography tips",
                imageRes = R.drawable.desert_stars,
                category = listOf("night", "extreme"),
                priceLevel = 2,
                duration = "3 hours",
                equipment = listOf("Water", "Hat", "Jacket"),
                requirements = listOf("Reservation required")
            ),
            Activity(
                id = "cave_tour",
                name = "Cave Exploration",
                description = "Discover cool underground formations",
                detailedDescription = "Guided cave system tour featuring:\n\n• Safety equipment\n• Geological explanations\n• Unique rock formations\n• Constant 15°C temperature\n• Photography opportunities",
                imageRes = R.drawable.cave_tour,
                category = listOf("geology", "adventure"),
                priceLevel = 1,
                duration = "2 hours",
                equipment = listOf("Sturdy shoes", "Light jacket"),
                requirements = listOf("Moderate fitness level")
            ),
            Activity(
                id = "waterpark",
                name = "Waterpark Day",
                description = "Beat the heat with thrilling water slides",
                detailedDescription = "Full-day waterpark access including:\n\n• 20+ attractions\n• Locker rentals\n• Shaded areas\n• Food options\n• Family-friendly zones",
                imageRes = R.drawable.waterpark,
                category = listOf("water", "family"),
                priceLevel = 3,
                duration = "Full day",
                equipment = listOf("Swimsuit", "Sunscreen", "Towel"),
                requirements = listOf("Height restrictions apply")
            ),
            Activity(
                id = "ice_skating",
                name = "Indoor Ice Skating",
                description = "Cool off while skating in air-conditioned rinks",
                detailedDescription = "Year-round ice skating facility with:\n\n• Skate rentals\n• Beginner lessons\n• Music sessions\n• Snack bar\n• Spectator area",
                imageRes = R.drawable.ice_skating,
                category = listOf("sports", "indoor"),
                priceLevel = 1,
                duration = "1-2 hours",
                equipment = listOf("Warm socks", "Gloves"),
                bestTime = "Afternoons"
            )
        )

        // ==== Default Suggestions ====
        else -> listOf(
            Activity(
                id = "cafe_tour",
                name = "Local Café Tour",
                description = "Discover hidden gems in your city",
                detailedDescription = "Self-guided specialty coffee tour including:\n\n• Map with recommended route\n• Unique drink at each stop\n• Barista interactions\n• Coffee tasting notes\n• Neighborhood insights",
                imageRes = R.drawable.cafe_tour,
                category = listOf("food", "social"),
                priceLevel = 1,
                duration = "Flexible",
                bestTime = "Weekend mornings"
            ),
            Activity(
                id = "book_club",
                name = "Book Club Meeting",
                description = "Join a literary discussion group",
                detailedDescription = "Monthly book club gathering featuring:\n\n• Themed discussions\n• Author insights\n• Reading recommendations\n• Snacks and drinks\n• Friendly atmosphere",
                imageRes = R.drawable.book_club,
                category = listOf("indoor", "social"),
                priceLevel = 0,
                duration = "2 hours",
                bestTime = "Weekday evenings"
            ),
            Activity(
                id = "art_gallery",
                name = "Art Gallery Visit",
                description = "Explore current exhibitions",
                detailedDescription = "Rotating art exhibitions showcasing:\n\n• Local artists\n• Themed collections\n• Interactive displays\n• Artist talks\n• Curator insights",
                imageRes = R.drawable.art_gallery,
                category = listOf("culture", "indoor"),
                priceLevel = 1,
                duration = "1-2 hours",
                bestTime = "Weekday afternoons"
            ),
            Activity(
                id = "neighborhood_walk",
                name = "Neighborhood Walk",
                description = "Discover local architecture and history",
                detailedDescription = "Self-guided walking tour highlighting:\n\n• Historic buildings\n• Architectural styles\n• Famous residents\n• Hidden courtyards\n• Photo spots",
                imageRes = R.drawable.neighborhood_walk,
                category = listOf("outdoor", "educational"),
                priceLevel = 0,
                duration = "Flexible",
                equipment = listOf("Comfortable shoes", "Camera"),
                bestTime = "Daylight hours"
            )
        )
    }
}