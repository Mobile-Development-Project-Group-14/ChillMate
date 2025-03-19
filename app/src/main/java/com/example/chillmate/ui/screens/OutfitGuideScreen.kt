package com.example.chillmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chillmate.viewmodel.WeatherViewModel
import com.example.chillmate.viewmodel.WeatherUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.chillmate.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults


data class Outfit(val name: String, val description: String, val imageRes: Int)

@Composable
fun OutfitGuideScreen(navController: NavController, weatherViewModel: WeatherViewModel) {
    var weatherState by remember { mutableStateOf<WeatherUiState>(weatherViewModel.weatherUiState) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val state = weatherViewModel.weatherUiState
            if (state is WeatherUiState.Success) {
                weatherState = state
            }
        }
    }

    val temperature = when (weatherState) {
        is WeatherUiState.Success -> (weatherState as WeatherUiState.Success).data.current.temperature_2m
        else -> 20.0 // Default temperature if data is unavailable
    }

    val outfitSuggestions = remember { getOutfitSuggestions(temperature) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {


        Spacer(modifier = Modifier.height(32.dp)) // Adds extra space before the heading
        Text(
            text = "Style It Right!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        LazyColumn(modifier = Modifier.weight(1f)) {
            items(outfitSuggestions) { outfit ->
                OutfitItem(outfit)
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Button(
            onClick = { /* Navigate to buy link */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Stay Cozy, Click to Buy!", color = Color.White)
        }
    }
}

@Composable
fun OutfitItem(outfit: Outfit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(outfit.imageRes),
                contentDescription = outfit.name,
                contentScale = ContentScale.Inside, // Ensures full image is visible
                modifier = Modifier.fillMaxWidth().height(450.dp) // Increased height
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = outfit.name,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = outfit.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}



fun getOutfitSuggestions(temperature: Double): List<Outfit> {
    return when {
        temperature < -6 -> listOf(
            Outfit("Baby, it's cold outside! Bundle up like a pro.", "For extreme cold weather dress with a heavy winter coat, thermal layers, wool scarf, gloves, beanie, insulated boots.", R.drawable.extremecold)
        )
        temperature in -6.0..0.0 -> listOf(
            Outfit("Cold hands, warm heart, and a cozy winter jacket", "Stay warm in freezing temps with a winter coat, sweater, gloves, thick pants, wool socks, warm boots", R.drawable.verycold)
        )
        temperature in 1.0..5.0 -> listOf(
            Outfit("Layer up! Cold weather is just an excuse for stylish outfits", "A good balance of warmth with a warm jacket, long sleeves, scarf, jeans, ankle boots", R.drawable.cold)
        )
        temperature in 6.0..10.0 -> listOf(
            Outfit("A light jacket and a warm smile are all you need!", "A Light jacket, hoodie, jeans and sneakers are perfect for chilly days.", R.drawable.chilly)
        )
        temperature in 11.0..15.0 -> listOf(
            Outfit("Sweater weather is better weather!", "Be Cozy and stylish with a Sweater, t-shirt, jeans, light sneakers.", R.drawable.cool)
        )
        temperature in 16.0..20.0 -> listOf(
            Outfit("The perfect balance – not too hot, not too cold!", "Want a simple and cool option? Wear a T-shirt, light sweater, jeans or chinos, sneakers.", R.drawable.mild)
        )
        temperature in 21.0..25.0 -> listOf(
            Outfit("Time to show off those summer vibes!", "A short-sleeve shirt, shorts, sandals or sneakers, sunglasses will keep you cool in warm weather.", R.drawable.warm)
        )
        temperature in 26.0..30.0 -> listOf(
            Outfit("Sun’s out, shades on, and light clothes all day!", "Stay cool in the heat with a tank top, cotton shorts, sandals, hat, sunglasses", R.drawable.hot)
        )
        else -> listOf(
            Outfit("Stay cool, stay hydrated, and dress for the heat!", "Loose cotton clothes, sun hat, UV-protection sunglasses, sandals are perfect for extreme heat.", R.drawable.veryhot)
        )
    }
}

