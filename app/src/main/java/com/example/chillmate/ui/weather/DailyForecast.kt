package com.example.chillmate.ui.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chillmate.model.DailyWeather
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ConstantLocale")
private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
@SuppressLint("ConstantLocale")
private val outputDateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())



// DailyForecast.kt
@Composable
fun DailyForecast(daily: DailyWeather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
    precipitation: Double

) {
    val formattedDate = try {
        outputDateFormat.format(inputDateFormat.parse(date)!!)
    } catch (e: Exception) {
        date // Fallback to the original date string if parsing fails
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formattedDate,
            color = Color.White
        )

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