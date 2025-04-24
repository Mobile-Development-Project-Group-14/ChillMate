package com.example.chillmate.ui.screens

import android.R
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chillmate.model.WeatherData
import com.example.chillmate.ui.components.ChillMateScaffold
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.viewmodel.TravelGuideViewModel
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TravelGuideScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel,
    travelGuideViewModel: TravelGuideViewModel
) {
    val isDay = when (val state = weatherViewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true
    }

    LaunchedEffect(Unit) {
        travelGuideViewModel.clearState()
    }

    ChillMateScaffold(
        navController = navController,
        isDay = isDay,
        title = "Travel Guide",
        showBackButton = true
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.getBackgroundGradient(isDay))
                .padding(paddingValues)
        ) {
            TravelGuideContent(travelGuideViewModel, isDay)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TravelGuideContent(
    viewModel: TravelGuideViewModel,
    isDay: Boolean
) {
    LocalContext.current
    var locationQuery by remember { mutableStateOf("") }
    val cities by viewModel.cities.collectAsState()
    val selectedDates by viewModel.selectedDates.collectAsState()
    val weatherData by viewModel.weatherData.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                errorMessage?.let { msg ->
                    Text(
                        text = msg,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = locationQuery,
                    onValueChange = {
                        locationQuery = it
                        if (it.length > 2) viewModel.searchCities(it)
                    },
                    label = { Text("Destination") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            null,
                            tint = AppTheme.getTextColor(isDay)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = AppTheme.getTextColor(isDay),
                        unfocusedTextColor = AppTheme.getTextColor(isDay),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                cities.forEach { city ->
                    CitySuggestionItem(
                        name = city.name,
                        country = city.country,
                        isDay = isDay,
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
                },
                isDay = isDay
            )
        }

        item {
            Button(
                onClick = { viewModel.generateTravelGuide() },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDates != null && locationQuery.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.getButtonColor(isDay),
                    contentColor = AppTheme.getButtonTextColor(isDay)
                )
            ) {
                Text("Generate Travel Guide", fontWeight = FontWeight.Bold)
            }
        }

        weatherData?.let { data ->
            item {
                WeatherRecommendations(weatherData = data, isDay = isDay)
            }
        }
    }
}

@Composable
private fun CitySuggestionItem(
    name: String,
    country: String,
    isDay: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.getCardColor(isDay))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.getTextColor(isDay)
            )
            Text(
                text = country,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.getSecondaryTextColor(isDay)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePicker(
    selectedDates: Pair<Calendar, Calendar>?,
    onDatesSelected: (Calendar, Calendar) -> Unit,
    isDay: Boolean
) {
    val today = LocalDate.now()
    val maxDate = today.plusDays(16)

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Travel Dates",
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.getTextColor(isDay)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            DatePickerButton(
                label = "Start Date",
                date = selectedDates?.first,
                isDay = isDay,
                onClick = { showStartDatePicker = true },


            )
            Spacer(modifier = Modifier.width(16.dp))
            DatePickerButton(
                label = "End Date",
                date = selectedDates?.second,
                isDay = isDay,
                onClick = { showEndDatePicker = true }
            )
        }
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDates?.first?.timeInMillis,
            yearRange = IntRange(today.year, maxDate.year)
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = millis
                            }
                            val endDate = selectedDates?.second ?: calendar
                            if (endDate.before(calendar)) {
                                onDatesSelected(calendar, calendar)
                            } else {
                                onDatesSelected(calendar, endDate)
                            }
                        }
                        showStartDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        selectedDates?.first?.let { startDate ->
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDates.second.timeInMillis,
                yearRange = IntRange(today.year, maxDate.year)
            )
            DatePickerDialog(
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = millis
                                }
                                onDatesSelected(startDate, calendar)
                            }
                            showEndDatePicker = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
private fun DatePickerButton(
    label: String,
    date: Calendar?,
    isDay: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.getButtonColor(isDay),
            contentColor = AppTheme.getButtonTextColor(isDay)
        )
    ) {
        Text(
            text = date?.let {
                "${it.get(Calendar.DAY_OF_MONTH)}/${it.get(Calendar.MONTH) + 1}/${it.get(Calendar.YEAR)}"
            } ?: label,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun WeatherRecommendations(
    weatherData: WeatherData,
    isDay: Boolean
) {
    val recommendations = remember(weatherData) {
        generateRecommendations(weatherData)
    }

    Column {
        Text(
            text = "Travel Essentials",
            style = MaterialTheme.typography.headlineSmall,
            color = AppTheme.getTextColor(isDay),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        recommendations.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.getCardColor(isDay))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.getTextColor(isDay)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    category.items.forEach { item ->
                        Text(
                            text = "• $item",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.getTextColor(isDay),
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun generateRecommendations(weatherData: WeatherData): List<RecommendationCategory> {
    val recommendations = mutableListOf<RecommendationCategory>()

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

    if (weatherData.daily.precipitation_sum.any { it > 0 }) {
        recommendations.add(
            RecommendationCategory(
                "Rain Protection",
                listOf("Umbrella", "Waterproof jacket", "Waterproof shoes")
            )
        )
    }

    if (weatherData.daily.uv_index_max.any { it > 3 }) {
        recommendations.add(
            RecommendationCategory(
                "Sun Protection",
                listOf("Sunglasses", "Sun hat", "Sunscreen SPF 50+")
            )
        )
    }

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

// Extension functions
@RequiresApi(Build.VERSION_CODES.O)
private fun Pair<LocalDate, LocalDate>.toCalendarPair(): Pair<Calendar, Calendar> {
    val start = Calendar.getInstance().apply {
        set(first.year, first.monthValue - 1, first.dayOfMonth)
    }
    val end = Calendar.getInstance().apply {
        set(second.year, second.monthValue - 1, second.dayOfMonth)
    }
    return start to end
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Calendar.toLocalDate(): LocalDate {
    return LocalDate.of(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
}