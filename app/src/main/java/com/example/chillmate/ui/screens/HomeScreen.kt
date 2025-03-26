package com.example.chillmate.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chillmate.R
import com.example.chillmate.model.WeatherData
import com.example.chillmate.ui.ErrorScreen
import com.example.chillmate.ui.LoadingScreen
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel
import androidx.compose.animation.core.tween
import com.example.chillmate.ui.weather.DailyForecast

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    when (val state = viewModel.weatherUiState) {
        WeatherUiState.Loading -> LoadingScreen()
        is WeatherUiState.Success -> WeatherContent(
            data = state.data,
            location = viewModel.currentLocation,
            navController = navController,
            viewModel = viewModel
        )
        WeatherUiState.Error -> ErrorScreen()
    }
}

// Function to get the appropriate Lottie animation based on weather conditions
fun getWeatherAnimation(weatherCode: Int, isDay: Boolean): Int {
    return when (weatherCode) {
        0 -> if (isDay) R.raw.day_clear_sky else R.raw.night_clear_sky
        1, 2, 3 -> if (isDay) R.raw.day_cloudy else R.raw.night_cloudy
        45, 48 -> R.raw.day_fog
        51, 53, 55 -> if (isDay) R.raw.day_rain else R.raw.night_rain
        56, 57 -> if (isDay) R.raw.day_rain else R.raw.night_rain
        61, 63, 65 -> if (isDay) R.raw.day_rain else R.raw.night_rain
        66, 67 -> if (isDay) R.raw.day_rain else R.raw.night_rain
        71, 73, 75 -> if (isDay) R.raw.day_snow else R.raw.night_snow
        77 -> if (isDay) R.raw.day_snow else R.raw.night_snow
        80, 81, 82 -> if (isDay) R.raw.day_rain else R.raw.night_rain
        85, 86 -> if (isDay) R.raw.day_snow else R.raw.night_snow
        95, 96, 99 -> if (isDay) R.raw.day_thunderstorm else R.raw.night_thunderstorm
        else -> if (isDay) R.raw.day_clear_sky else R.raw.night_clear_sky // Default
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    data: WeatherData,
    navController: NavController,
    location: Pair<Double, Double>,
    viewModel: WeatherViewModel
) {
    // Determine if it's day or night based on the API data
    val isDay = data.current.is_day == 1
    // Day/Night color definitions
    val dayColors = listOf(
        Color(0xFFC0DEFF),  // Light blue
        Color(0xFF74B6FF),  // Medium blue
        Color(0xFF419BFF)   // Dark blue
    )

    val nightColors = listOf(
        Color(0xFF695A5A), // Dark brown
        Color(0xFF9F6060), // Medium brown
        Color(0xFF6B4F4F)  // Light brown
    )

    val animatedBackground by animateColorAsState(
        targetValue = if (isDay) dayColors[0] else nightColors[0],
        animationSpec = tween(durationMillis = 1000)
    )

    val animatedGradient = Brush.verticalGradient(
        colors = listOf(
            animatedBackground,
            if (isDay) dayColors[1] else nightColors[1],
            if (isDay) dayColors[2] else nightColors[2]
        ),
        startY = 0f,
        endY = 1000f
    )

    // Fetch location name when coordinates change
    LaunchedEffect(location) {
        viewModel.updateLocationName(location.first, location.second)
    }

    // Observe the location name state
    val locationName by viewModel.locationName.collectAsState()

    // Get the appropriate Lottie animation based on weather conditions
    val animationResId = getWeatherAnimation(data.current.weather_code, isDay)

    // Load Lottie composition
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(animationResId)
    )

    // Scaffold to structure the screen with a top bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ChillMate",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDay) dayColors[2] else nightColors[1], // Match the gradient color
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(animatedGradient)
            ) {
                Column(
                    modifier = modifier
                        .padding(paddingValues) // Apply padding from Scaffold
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = locationName, // Hardcoded location
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    // Top Row: Temperature and Weather Animation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left Column: Temperature and Apparent Temperature
                        Column {
                            Text(
                                text = "${data.current.temperature_2m}${data.current_units.temperature_2m}",
                                style = MaterialTheme.typography.displayLarge,
                                color = Color.White
                            )
                            Text(
                                text = "Feels like ${data.current.apparent_temperature}${data.current_units.temperature_2m}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }

                        // Right Column: Weather Animation
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }

                    DailyForecast(
                        daily = data.daily,
                        isDay = isDay
                    )

                    // Girl Image for Outfit Guide
                    Image(
                        painter = painterResource(id = viewModel.currentOutfitImage),
                        contentDescription = "Outfit Guide Image",
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight(0.4f)
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate("outfitGuide")
                            },
                        contentScale = ContentScale.Fit
                    )

                    // Buttons for Navigation
                    Button(
                        onClick = { navController.navigate("outfitGuide") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4B6CB7)
                        )
                    ) {
                        Text("See Full Outfit Guide")
                    }

                    Button(
                        onClick = { navController.navigate("todayActivity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4B6CB7)
                        )
                    ) {
                        Text("What to Do Today")
                    }
                }
            }
        }
    )
}

