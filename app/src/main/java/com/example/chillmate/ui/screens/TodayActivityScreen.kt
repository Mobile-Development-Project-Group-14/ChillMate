package com.example.chillmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chillmate.R
import com.example.chillmate.model.Activity
import com.example.chillmate.model.WeatherCondition
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel


//Hardcoded data for weather data
val todayWeather = WeatherCondition(
    location = "Oulu", //City name
    type = "snow",  //Weather condition
    icon = "❄️", //Weather icon
    temperature = -5, //Temperature in Celsius
    isDay = true //Daytime
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayActivityScreen(navController: NavController, viewModel: WeatherViewModel) {
    val isDay = when (val state = viewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Activities", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.getTopBarColor(isDay),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.getBackgroundGradient(isDay))
                .padding(paddingValues)
        ) {
            val weatherCondition = viewModel.weatherUiState.let { state ->
                if (state is WeatherUiState.Success) {
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
                    WeatherCondition( // Provide default values
                        location = "Unknown",
                        type = "clear",
                        icon = "☀️",
                        temperature = 20,
                        isDay = true
                    )
                }
            }

            ActivityList(activities = getActivitySuggestions(weatherCondition))
        }
    }
}

@Composable
fun WeatherInfo(weather: WeatherCondition) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()

    ) {
        Text(
            text = weather.icon,
            fontSize = 48.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))
        //Display Location
        Text(
            text = weather.location,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))
        //Display Temperature
        Text(
            text = "${weather.temperature}°C",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White

        )

        Spacer(modifier = Modifier.height(8.dp))
        //Display Weather Condition
        Text(
            text = weather.type.replaceFirstChar { it.uppercase() },
            fontSize = 24.sp,
            color = Color.White.copy(alpha = 0.8f)
        )


    }
}

@Composable
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

@Composable
fun ActivityList(activities: List<Activity>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {

        //Group activities by category
        val groupedActivities = activities.groupBy { it.category.first() }

        groupedActivities.forEach { (category, items) ->
            item {
                Text(
                    text = "${category.replaceFirstChar { it.uppercase() }} Activities",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(items) {activity ->
                ActivityCard(activity=activity, onClick = {/*Handle click event*/})
                Spacer(modifier = Modifier.height(8.dp))

            }

        }

    }
}

@Composable
fun ActivityCard(activity: Activity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {onClick()}
                    .padding(8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f),
            contentColor = Color.White
        ),

        elevation  = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {Box(
            modifier = Modifier
                .size(width = 120.dp, height = 120.dp)
                .clip(MaterialTheme.shapes.medium)

        )
        {
            Image(
                painter = painterResource(id = activity.imageRes),
                contentDescription = activity.name,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

            Spacer(modifier = Modifier.width(16.dp))

            Column (
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                verticalArrangement = Arrangement.SpaceBetween

            ) {
                Column {
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                    Spacer(modifier = Modifier.height(4.dp))
                }


            }
        }
    }
}

