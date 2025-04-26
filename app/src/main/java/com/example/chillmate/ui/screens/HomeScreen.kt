
package com.example.chillmate.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.chillmate.ui.weather.DailyForecast
import com.example.chillmate.ui.weather.getActivitySuggestions
import com.example.chillmate.ui.weather.getCurrentWeatherCondition
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chillmate.R
import com.example.chillmate.model.WeatherData
import com.example.chillmate.ui.ErrorScreen
import com.example.chillmate.ui.LoadingScreen
import com.example.chillmate.ui.components.ImageCard
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.ui.theme.AppTheme.dayColors
import com.example.chillmate.ui.theme.AppTheme.nightColors
import com.example.chillmate.viewmodel.AlertSeverity
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    val activeAlerts by viewModel.activeAlerts.collectAsState()
    val isDay = when (val state = viewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true
    }

    var showAlerts by remember { mutableStateOf(false) }

    // Launch effect to handle the 5-second delay
    LaunchedEffect(viewModel.weatherUiState) {
        if (viewModel.weatherUiState is WeatherUiState.Success) {
            delay(5000) // Wait 5 seconds
            showAlerts = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = viewModel.weatherUiState) {
            WeatherUiState.Loading -> LoadingScreen()
            is WeatherUiState.Success -> WeatherContent(
                data = state.data,
                navController = navController,
                viewModel = viewModel
            )
            WeatherUiState.Error -> ErrorScreen()
        }

        if (showAlerts && activeAlerts.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = {
                    showAlerts = false
                    viewModel.dismissAlerts() },
                modifier = Modifier
                        .padding(16.dp),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                ),
                shape = MaterialTheme.shapes.medium,
                containerColor = if (isDay) dayColors[1].copy(alpha = 0.9f)
                else nightColors[1].copy(alpha = 0.85f),
                text = {
                    Column(
                        //modifier = Modifier
                            /*.background(
                                color = if (isDay) MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                                shape = MaterialTheme.shapes.extraLarge
                            )*/
                    )
                    {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp)
                                .weight(1f,fill = false)
                        ) {

                            Text(
                                text = "Weather Alerts",
                                style = MaterialTheme.typography.headlineSmall,
                                color = AppTheme.getAlertTextColor(isDay),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            activeAlerts.forEachIndexed { index, alert ->
                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                    val color = when (alert.severity) {
                                        AlertSeverity.SEVERE -> MaterialTheme.colorScheme.error
                                        AlertSeverity.MODERATE -> Color(0xFFFFA500)
                                        AlertSeverity.MINOR -> MaterialTheme.colorScheme.tertiary
                                    }

                                    Text(
                                        text = alert.title,
                                        color = color,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = alert.description,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    if (index < activeAlerts.lastIndex) {
                                        androidx.compose.material3.HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                            thickness = 1.dp
                                        )
                                    }

                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.dismissAlerts() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(78.dp)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    Color.White.copy(alpha = 0.3f),

                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Got it")
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(
    data: WeatherData,
    navController: NavController,
    viewModel: WeatherViewModel
) {
    val isDay = data.current.is_day == 1
    val animatedGradient = Brush.verticalGradient(
        colors = if (isDay) dayColors else nightColors,
        startY = 0f,
        endY = 1000f
    )
    val locationName by viewModel.locationName.collectAsState()
    val animationResId = getWeatherAnimation(data.current.weather_code, isDay)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))

    //Get weather conditions and activities
    val weatherCondition= getCurrentWeatherCondition(viewModel)
    val activities = remember { getActivitySuggestions(weatherCondition) }
    var currentSlide by remember { mutableIntStateOf(0) }

    //Auto-slide effect
    LaunchedEffect(Unit) {
        while (true) {
        delay(7000)
        currentSlide= (currentSlide + 1) % activities.size
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChillMate", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDay) dayColors[2] else nightColors[1],
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { /* Menu */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.chill_mate_icon),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(24.dp)
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
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(locationName, style = MaterialTheme.typography.headlineMedium, color = Color.White)

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Column {
                            Text(
                                "${data.current.temperature_2m.toInt()}${data.current_units.temperature_2m}",
                                style = MaterialTheme.typography.displayLarge,
                                color = Color.White
                            )
                            Text(
                                "Feels like ${data.current.apparent_temperature.toInt()}${data.current_units.temperature_2m}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                        LottieAnimation(
                            composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    DailyForecast(daily = data.daily)

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        ImageCard(
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            imageRes = viewModel.currentOutfitImage,
                            onClick = { navController.navigate("outfitGuide") },
                            contentDescription = "Outfit Guide",
                        )

                        ImageCard(
                            imageRes = activities[currentSlide].imageRes,
                            onClick = { navController.navigate("todayActivity") },
                            contentDescription = activities[currentSlide].name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            title = activities[currentSlide].name,
                            description = activities[currentSlide].description,
                            showSlideIndicators = true,
                            totalSlides = activities.size,
                            currentSlide = currentSlide
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        TravelGuideCard(
                            onClick = { navController.navigate("travelGuide") },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

fun getWeatherAnimation(weatherCode: Int, isDay: Boolean): Int {
    return when (weatherCode) {
        0 -> if (isDay) R.raw.day_clear_sky else R.raw.night_clear_sky
        1, 2, 3 -> if (isDay) R.raw.day_cloudy else R.raw.night_cloudy
        45, 48 -> R.raw.day_fog
        51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 ->
            if (isDay) R.raw.day_rain else R.raw.night_rain
        71, 73, 75, 77, 85, 86 ->
            if (isDay) R.raw.day_snow else R.raw.night_snow
        95, 96, 99 ->
            if (isDay) R.raw.day_thunderstorm else R.raw.night_thunderstorm
        else -> if (isDay) R.raw.day_clear_sky else R.raw.night_clear_sky
    }
}

@Composable
fun TravelGuideCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.travel_animation))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                color = Color.White.copy(alpha = 0.07f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(80.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "Travel Guide",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = "Explore the world with our travel guide",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}