package com.example.chillmate.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chillmate.model.WeatherData
import com.example.chillmate.viewmodel.TravelGuideViewModel
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TravelGuideScreen(
    viewModel: TravelGuideViewModel
) {
    var locationQuery by remember { mutableStateOf("") }
    val cities by viewModel.cities.collectAsState()
    val selectedDates by viewModel.selectedDates.collectAsState()
    val weatherData by viewModel.weatherData.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                OutlinedTextField(
                    value = locationQuery,
                    onValueChange = {
                        locationQuery = it
                        if (it.length > 2) viewModel.searchCities(it)
                    },
                    label = { Text("Destination") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                cities.forEach { city ->
                    CitySuggestionItem(
                        name = city.name,
                        country = city.country,
                        onClick = {
                            locationQuery = "${city.name}, ${city.country}"
                            viewModel.selectCity(city)
                        }
                    )
                }
            }
        }

        item {
            DateRangePicker(
                selectedDates = selectedDates?.toCalendarPair(),
                onDatesSelected = { start, end ->
                    viewModel.setDates(start.toLocalDate(), end.toLocalDate())
                }
            )
        }

        item {
            Button(
                onClick = { viewModel.generateTravelGuide() },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDates != null && locationQuery.isNotEmpty()
            ) {
                Text("Generate Travel Guide")
            }
        }

        weatherData?.let { data ->
            item {
                WeatherRecommendations(weatherData = data)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Pair<LocalDate, LocalDate>?.toCalendarPair(): Pair<Calendar, Calendar>? {
    return this?.let { (first, second) ->
        Pair(first.toCalendar(), second.toCalendar())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun LocalDate.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        set(year, monthValue - 1, dayOfMonth)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Calendar.toLocalDate(): LocalDate {
    return LocalDate.of(
        get(Calendar.YEAR),
        get(Calendar.MONTH) + 1,
        get(Calendar.DAY_OF_MONTH)
    )
}

@Composable
private fun CitySuggestionItem(
    name: String,
    country: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(country, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePicker(
    selectedDates: Pair<Calendar, Calendar>?,
    onDatesSelected: (Calendar, Calendar) -> Unit
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Column {
        Text("Travel Dates", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            DatePickerButton(
                label = "Start Date",
                date = selectedDates?.first,
                onClick = { showStartDatePicker = true }
            )
            Spacer(modifier = Modifier.width(16.dp))
            DatePickerButton(
                label = "End Date",
                date = selectedDates?.second,
                onClick = { showEndDatePicker = true }
            )
        }
    }

    if (showStartDatePicker) {
        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDates?.first?.timeInMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateState.selectedDateMillis?.let {
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = it
                            }
                            onDatesSelected(calendar, selectedDates?.second ?: calendar)
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (showEndDatePicker) {
        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDates?.second?.timeInMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateState.selectedDateMillis?.let {
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = it
                            }
                            onDatesSelected(selectedDates?.first ?: calendar, calendar)
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}

@Composable
private fun DatePickerButton(
    label: String,
    date: Calendar?,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = date?.let {
            "${it.get(Calendar.DAY_OF_MONTH)}/${it.get(Calendar.MONTH) + 1}/${it.get(Calendar.YEAR)}"
        } ?: label)
    }
}

@Composable
private fun WeatherRecommendations(weatherData: WeatherData) {
    val recommendations = remember(weatherData) {
        generateRecommendations(weatherData)
    }

    Column {
        Text("Travel Essentials", style = MaterialTheme.typography.headlineSmall)

        recommendations.forEach { category ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(category.title, style = MaterialTheme.typography.titleMedium)
                category.items.forEach { item ->
                    Text("• $item", modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

private fun generateRecommendations(weatherData: WeatherData): List<RecommendationCategory> {
    val recommendations = mutableListOf<RecommendationCategory>()

    // Temperature-based recommendations
    val avgTemp = weatherData.daily.temperature_2m_max.average()
    recommendations.add(
        RecommendationCategory(
            "Clothing",
            when {
                avgTemp < 10 -> listOf("Thermal underwear", "Warm jacket", "Gloves", "Scarf")
                avgTemp < 20 -> listOf("Light jacket", "Long pants", "Sweaters")
                else -> listOf("T-shirts", "Shorts", "Light dresses")
            }
        )
    )

    // Precipitation recommendations
    if (weatherData.daily.precipitation_sum.any { it > 0 }) {
        recommendations.add(
            RecommendationCategory(
                "Rain Protection",
                listOf("Umbrella", "Waterproof jacket", "Waterproof shoes")
            )
        )
    }

    // Sun protection
    if (weatherData.daily.uv_index_max.any { it > 3 }) {
        recommendations.add(
            RecommendationCategory(
                "Sun Protection",
                listOf("Sunglasses", "Sun hat", "Sunscreen SPF 50+")
            )
        )
    }

    // Always include essentials
    recommendations.add(
        RecommendationCategory(
            "Essentials",
            listOf("Passport/ID", "Travel adapter", "First aid kit", "Reusable water bottle")
        )
    )

    return recommendations
}

private data class RecommendationCategory(
    val title: String,
    val items: List<String>
)