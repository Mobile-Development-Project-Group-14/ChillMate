package com.example.chillmate.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun getWeatherAnimation(weatherData: WeatherData, isDay: Boolean): Int {
    return when {
        weatherData.current.precipitation > 0 -> {
            if (isDay) R.raw.day_rain else R.raw.night_rain
        }
        weatherData.current.snowfall > 0 -> {
            if (isDay) R.raw.day_snow else R.raw.night_snow
        }
        weatherData.current.cloud_cover > 50 -> R.raw.cloudy
        else -> R.raw.clear_sky // Default animation for clear weather
    }
}

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    data: WeatherData,
    navController: NavController,
    location: Pair<Double, Double>,
    viewModel: WeatherViewModel
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFC0DEFF),  // Light blue
            Color(0xFF74B6FF),  // Medium blue
            Color(0xFF419BFF)   // Dark blue

        ),
        startY = 0f,
        endY = 1000f
    )

    // Determine if it's day or night based on the API data
    val isDay = data.current.is_day == 1

    // Get the appropriate Lottie animation based on weather conditions
    val animationResId = getWeatherAnimation(data, isDay)

    // Load Lottie composition
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(animationResId)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = modifier
                .padding(top = 24.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Oulu/Toppila", // Hardcoded location
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
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                // Right Column: Weather Animation
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate("outfitGuide")
                        },
                    contentScale = ContentScale.Fit
                )
            }

            // Girl Image for Outfit Guide
            Image(
                painter = painterResource(id = viewModel.currentOutfitImage), // Load the image
                contentDescription = "Weather Condition Image", // Accessibility description
                modifier = Modifier
                    .fillMaxWidth(0.4f) // Half of the screen width
                    .fillMaxHeight(0.4f)
                    .align(Alignment.CenterHorizontally) // Center the image
                    .padding(vertical = 8.dp) // Add padding around the image
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { // Make the image clickable
                        navController.navigate("outfitGuide") // Navigate to Outfit Guide
                    }, // Rounded corners
                contentScale = ContentScale.Fit // Adjust image scaling
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

@Composable
fun WeatherInfoItem(label: String, value: String, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}