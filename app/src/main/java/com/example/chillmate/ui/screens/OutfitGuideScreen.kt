package com.example.chillmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
    val accessoryItems = remember { getAccessoryItems(temperature) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Style It Right!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ClothingCarousel(accessoryItems)

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
fun ClothingCarousel(accessoryItems: List<Outfit>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(accessoryItems) { accessory ->
            ClothingItemCard(accessory)
        }
    }
}

@Composable
fun ClothingItemCard(outfit: Outfit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003366)),
        modifier = Modifier
            .width(140.dp)
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(outfit.imageRes),
                contentDescription = outfit.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
            )
            Text(
                text = outfit.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun OutfitItem(outfit: Outfit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF003366),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(outfit.imageRes),
                contentDescription = outfit.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
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

fun getAccessoryItems(temperature: Double): List<Outfit> {
    return when {
        temperature < -15 -> listOf(
            Outfit("Wool Scarf", "Cozy up with a warm scarf.", R.drawable.scarf),
            Outfit("Insulated Boots", "Keep feet toasty.", R.drawable.wintershoes),
            Outfit("Wool Hat", "Trap the warmth.", R.drawable.winterhat),
            Outfit("Mittens", "No cold fingers here!", R.drawable.mittens),
            Outfit("Winter Gloves", "More warmer!", R.drawable.wintergloves),
            Outfit("Winter Jacket", "Cozy!", R.drawable.winterjacket),
            Outfit("Thermal layers", "Layer up!", R.drawable.thermallayers),
            Outfit("wool socks", "Warm feets!", R.drawable.wintersocks)
        )
        temperature in -14.0..-10.0 -> listOf(
            Outfit("Scarf", "A stylish shield from cold.", R.drawable.scarf),
            Outfit("Warm Boots", "Essential winter footwear.", R.drawable.winterboot) ,
            Outfit("Winter Jacket", "Cozy!", R.drawable.warm_winter_coat),
            Outfit("Wool Hat", "Trap the warmth.", R.drawable.winterhat),
            Outfit("Mittens", "No cold fingers here!", R.drawable.wintergloves),
        )
        temperature in -9.9..-5.0 -> listOf(
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("Warm Boots", "Essential winter footwear.", R.drawable.ankle_boots) ,
            Outfit("Wool hat", "Cozy!", R.drawable.winterhat),
            Outfit("Warm sweater", "Wear a sweater inside.", R.drawable.sweater),
            Outfit("Gloves", "No cold fingers here!", R.drawable.wintergloves),
        )
        temperature in -4.9..0.0 -> listOf(
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("Warm Boots", "Essential winter footwear.", R.drawable.ankle_boots) ,
            Outfit("scarf", "shield the wind!", R.drawable.scarf),
            Outfit("Warm sweater", "Wear a sweater inside.", R.drawable.sweater),

        )
        temperature in 0.1..5.0 -> listOf(
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("shoes", "Essential  footwear.", R.drawable.sneakers) ,
            Outfit("Hoddie", "Beat the cold!", R.drawable.hoddie),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),

            )
        temperature in 5.1..10.0 -> listOf(
            Outfit("shoes", "Essential  footwear.", R.drawable.sneakers) ,
            Outfit("Sweater", "Beat the cold!", R.drawable.sweater),
            Outfit("Hoddie", "Beat the cold!", R.drawable.hoddie),
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses)
        )
        temperature in 10.1..15.0 -> listOf(
            Outfit("shoes", "Essential  footwear.", R.drawable.sneakers) ,
            Outfit("long sleeve", "Beat the cold!", R.drawable.longsleeve),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
            Outfit("Light Jacket", "A stylish jacket.", R.drawable.verylightjacket),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses)
        )
        temperature in 15.1..22.0 -> listOf(
            Outfit("Short", "Feel the sun.", R.drawable.shortdenim),
            Outfit("T-shirt", "Feel the sun.", R.drawable.tshirt),
            Outfit("Sandals", "Open comfort.", R.drawable.sandals),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
        )
        temperature in 22.1..27.0 -> listOf(
            Outfit("sun hat", "Keep the sun out of your eyes.", R.drawable.hat),
            Outfit("Short", "Feel the sun.", R.drawable.shortdenim),
            Outfit("Tank top", "Feel the sun.", R.drawable.tanktop),
            Outfit("Sandals", "Open comfort.", R.drawable.sandals),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses)
        )
        else -> listOf(
            Outfit("Cap", "Essential for strong sun.", R.drawable.cap),
            Outfit("Flip Flops", "Stay cool and casual.", R.drawable.flipflop),
            Outfit("Sunglasses", "Block that bright sun.", R.drawable.sunglasses)
        )
    }
}




fun getOutfitSuggestions(temperature: Double): List<Outfit> {
    return when {
        temperature < -15 -> listOf(
            Outfit("Baby, it's cold outside! Bundle up like a pro.", "For extreme cold weather dress with a heavy winter coat, thermal layers, wool scarf, gloves, beanie, insulated boots.", R.drawable.extremecold)
        )
        temperature in -14.0..-10.0 -> listOf(
            Outfit("Cold hands, warm heart, and a cozy winter jacket", "Stay warm in freezing temps with a winter coat, sweater, gloves, thick pants, wool socks, warm boots", R.drawable.cold3)
        )
        temperature in -9.9..-5.0 -> listOf(
            Outfit("Cold hands, warm heart, and a cozy winter jacket", "Stay warm in freezing temps with a winter coat, sweater, gloves, thick pants, wool socks, warm boots", R.drawable.verycold)
        )
        temperature in -4.9..0.0 -> listOf(
            Outfit("Layer up! Cold weather is just an excuse for stylish outfits", "A good balance of warmth with a warm jacket, long sleeves, scarf, jeans, ankle boots", R.drawable.cold)
        )
        temperature in 0.1..5.0 -> listOf(
            Outfit("A light jacket and a warm smile are all you need!", "A Light jacket, hoodie, jeans and sneakers are perfect for chilly days.", R.drawable.chilly)
        )
        temperature in 5.1..10.0 -> listOf(
            Outfit("Sweater weather is better weather!", "Be Cozy and stylish with a Sweater, t-shirt, jeans, light sneakers.", R.drawable.cool)
        )
        temperature in 10.1..15.0 -> listOf(
            Outfit("The perfect balance – not too hot, not too cold!", "Want a simple and cool option? Wear a T-shirt, light sweater, jeans or chinos, sneakers.", R.drawable.mild)
        )
        temperature in 15.1..22.0 -> listOf(
            Outfit("Time to show off those summer vibes!", "A short-sleeve shirt, shorts, sandals or sneakers, sunglasses will keep you cool in warm weather.", R.drawable.warm)
        )
        temperature in 22.1..27.0 -> listOf(
            Outfit("Sun’s out, shades on, and light clothes all day!", "Stay cool in the heat with a tank top, cotton shorts, sandals, hat, sunglasses", R.drawable.hot)
        )
        else -> listOf(
            Outfit("Stay cool, stay hydrated, and dress for the heat!", "Loose cotton clothes, sun hat, UV-protection sunglasses, sandals are perfect for extreme heat.", R.drawable.veryhot)
        )
    }
}

