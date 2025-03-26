package com.example.chillmate.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chillmate.model.DailyWeather
import com.example.chillmate.ui.screens.getWeatherAnimation
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

// DailyForecast.kt
@Composable
fun DailyForecast(daily: DailyWeather, isDay: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            daily.time.forEachIndexed { index, date ->
                DailyForecastItem(
                    date = date,
                    maxTemp = daily.temperature_2m_max[index],
                    minTemp = daily.temperature_2m_min[index],
                    precipitation = daily.precipitation_sum[index],
                    isDay = isDay
                )
            }
        }
    }
}

@Composable
private fun DailyForecastItem(
    date: String,
    maxTemp: Double,
    minTemp: Double,
    precipitation: Double,
    isDay: Boolean
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Format date as "Wed, Mar 27"
        Text(
            text = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
                .format(SimpleDateFormat("yyyy-MM-dd").parse(date)),
            color = Color.White
        )

        // Temperature range
        Text(
            text = "${maxTemp.roundToInt()}°/${minTemp.roundToInt()}°",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

        // Precipitation
        Text(
            text = "Precip: ${precipitation}mm",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}