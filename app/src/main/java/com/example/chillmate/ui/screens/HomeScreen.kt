package com.example.chillmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chillmate.model.WeatherData
import com.example.chillmate.ui.ErrorScreen
import com.example.chillmate.ui.LoadingScreen
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController, // Add NavController for navigation
    location: Pair<Double, Double>? // Add location parameter
) {
    when (val state = viewModel.weatherUiState) {
        is WeatherUiState.Loading -> LoadingScreen()
        is WeatherUiState.Success -> location?.let {
            WeatherContent(
                modifier = modifier,
                data = state.data,
                navController = navController,
                location = location

            )
        }
        is WeatherUiState.Error -> ErrorScreen()
    }
}

@Composable
fun WeatherContent(modifier: Modifier, data: WeatherData, navController: NavController, location: Pair<Double, Double>) {
    // Cool gradient for cold temperatures (customize colors as needed)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6B8DD6),  // Light blue
            Color(0xFF4B6CB7),  // Medium blue
        ),
        startY = 0f,
        endY = 1000f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = modifier
                .padding(top = 48.dp)  // Space for future top bar
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Current Weather",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            WeatherInfoItem(
                label = "Temperature",
                value = "${data.current.temperature_2m}${data.current_units.temperature_2m}",
                textColor = Color.White
            )

            WeatherInfoItem(
                label = "Humidity",
                value = "${data.current.relative_humidity_2m}${data.current_units.relative_humidity_2m}",
                textColor = Color.White
            )

            WeatherInfoItem(
                label = "Wind Speed",
                value = "${data.current.wind_speed_10m}${data.current_units.wind_speed_10m}",
                textColor = Color.White
            )

            WeatherInfoItem(
                label = "Precipitation",
                value = "${data.current.precipitation}${data.current_units.precipitation}",
                textColor = Color.White
            )
            // print location
            Text(
                text = "Location: ${location.first}, ${location.second}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            // Add Buttons for Navigation
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("outfitGuide") }, // Navigate to OutfitGuide
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
                onClick = { navController.navigate("todayActivity") }, // Navigate to TodayActivity
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

