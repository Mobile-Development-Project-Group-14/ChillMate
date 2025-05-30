package com.example.chillmate.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chillmate.R
import com.example.chillmate.model.TemperatureRange
import com.example.chillmate.model.getTemperatureRange
import com.example.chillmate.ui.components.ChillMateScaffold
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel


data class Outfit(val name: String, val description: String, val imageRes: Int)

@Composable
fun OutfitGuideScreen(navController: NavController, weatherViewModel: WeatherViewModel) {
    val isDay = when (val state = weatherViewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true
    }

    val temperature = when (val state = weatherViewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.temperature_2m
        else -> weatherViewModel.currentTemperature

    }

    ChillMateScaffold(
        navController = navController,
        isDay = isDay,
        title = "Outfit Guide"
    ) { paddingValues ->
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.getBackgroundGradient(isDay))
                    .padding(paddingValues)
                    .padding(16.dp)
        )   {
                Text(
                    text = "Style It Right!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ClothingCarousel(getAccessoryItems(temperature))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    itemsIndexed(getOutfitSuggestions(temperature)) { index, outfit ->
                        OutfitItem(
                            outfit = outfit,
                            isHighlighted = index == 1
                        )
                    }
                }

                Button(
                    onClick = { navController.navigate("shop") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.transparentButtonColor,
                        contentColor = Color.White,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Stay Cozy, Click to Buy!", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }


@Composable
private fun ClothingCarousel(accessoryItems: List<Outfit>) {
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
private fun ClothingItemCard(outfit: Outfit) {

    Box(
        modifier = Modifier
            .width(90.dp)
            .height(95.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
            Image(
                painter = painterResource(outfit.imageRes),
                contentDescription = outfit.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            )
            }
            Text(
                text = outfit.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
                )
            }
        }
    }

@Composable
private fun OutfitItem(outfit: Outfit, isHighlighted: Boolean = false) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = if (isHighlighted)0.3f else 0.2f) )
    ) {
       Column(modifier = Modifier.padding(16.dp)) {


           Image(
               painter = painterResource(outfit.imageRes),
               contentDescription = outfit.name,
               contentScale = ContentScale.FillWidth,
                modifier = Modifier
                     .fillMaxWidth()
                     .aspectRatio(2f / 3f)
                     .clip(RoundedCornerShape(8.dp))
           )
           Spacer(modifier = Modifier.height(12.dp))
           Text(
               text = outfit.name,
               fontWeight = FontWeight.Bold,
               fontSize = 22.sp,
               color = Color.White
           )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = outfit.description,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
              )
       }
    }
}


fun getAccessoryItems(temperature: Double): List<Outfit> {
    return when(getTemperatureRange(temperature)) {
        TemperatureRange.EXTREME_COLD -> listOf(
            Outfit("Wool Scarf", "Cozy up with a warm scarf.", R.drawable.scarf),
            Outfit("Insulated Boots", "Keep feet toasty.", R.drawable.wintershoes),
            Outfit("Wool Hat", "Trap the warmth.", R.drawable.winterhat),
            Outfit("Mittens", "No cold fingers here!", R.drawable.mittens),
            Outfit("Winter Gloves", "More warmer!", R.drawable.wintergloves),
            Outfit("Winter Jacket", "Cozy!", R.drawable.winterjacket),
            Outfit("Thermal layers", "Layer up!", R.drawable.thermallayers),
            Outfit("wool socks", "Warm feet!", R.drawable.wintersocks)
        )
        TemperatureRange.VERY_COLD -> listOf(
            Outfit("Scarf", "A stylish shield from cold.", R.drawable.scarf),
            Outfit("Warm Boots", "Essential winter footwear.", R.drawable.winterboot) ,
            Outfit("Winter Jacket", "Cozy!", R.drawable.warm_winter_coat),
            Outfit("Wool Hat", "Trap the warmth.", R.drawable.winterhat),
            Outfit("Mittens", "No cold fingers here!", R.drawable.wintergloves),
        )
        TemperatureRange.COLD -> listOf(
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("Warm Boots", "Essential winter footwear.", R.drawable.ankle_boots) ,
            Outfit("Wool hat", "Cozy!", R.drawable.winterhat),
            Outfit("Warm sweater", "Wear a sweater inside.", R.drawable.sweater),
            Outfit("Gloves", "No cold fingers here!", R.drawable.wintergloves),
        )
        TemperatureRange.CHILLY -> listOf(
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("Warm Boots", "Essential winter footwear.", R.drawable.ankle_boots) ,
            Outfit("scarf", "shield the wind!", R.drawable.scarf),
            Outfit("Warm sweater", "Wear a sweater inside.", R.drawable.sweater),

        )
        TemperatureRange.COOL -> listOf(
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("shoes", "Essential  footwear.", R.drawable.sneakers) ,
            Outfit("Hoodie", "Beat the cold!", R.drawable.hoddie),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),

            )
        TemperatureRange.MILD -> listOf(
            Outfit("shoes", "Essential  footwear.", R.drawable.sneakers) ,
            Outfit("Sweater", "Beat the cold!", R.drawable.sweater),
            Outfit("Hoodie", "Beat the cold!", R.drawable.hoddie),
            Outfit("Light Jacket", "A stylish shield from cold.", R.drawable.lightjacket),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses)
        )
        TemperatureRange.WARM -> listOf(
            Outfit("shoes", "Essential  footwear.", R.drawable.sneakers) ,
            Outfit("Sandals", "Open comfort.", R.drawable.sandals),
            Outfit("long sleeve", "Beat the cold!", R.drawable.longsleeve),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
            Outfit("Light Jacket", "A stylish jacket.", R.drawable.verylightjacket),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses)
        )
        TemperatureRange.HOT -> listOf(
            Outfit("Short", "Feel the sun.", R.drawable.shortdenim),
            Outfit("T-shirt", "Feel the sun.", R.drawable.tshirt),
            Outfit("Sandals", "Open comfort.", R.drawable.sandals),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
        )
        TemperatureRange.VERY_HOT -> listOf(
            Outfit("sun hat", "Keep the sun out of your eyes.", R.drawable.hat),
            Outfit("Short", "Feel the sun.", R.drawable.shortdenim),
            Outfit("Tank top", "Feel the sun.", R.drawable.tanktop),
            Outfit("Sandals", "Open comfort.", R.drawable.sandals),
            Outfit("Jeans", "Wear a trendy jean.", R.drawable.jeans),
            Outfit("Cap", "Essential for strong sun.", R.drawable.cap),
            Outfit("Flip Flops", "Stay cool and casual.", R.drawable.flipflop),
            Outfit("Sunglasses", "Stylish and protective.", R.drawable.sunglasses)
        )

    }
}




fun getOutfitSuggestions(temperature: Double): List<Outfit> {
    return when (getTemperatureRange(temperature)) {
        TemperatureRange.EXTREME_COLD  -> listOf(
            Outfit("Baby, it's cold outside! Bundle up like a pro.", "For extreme cold weather dress with a heavy winter coat, thermal layers, wool scarf, gloves, beanie, insulated boots.", R.drawable.extremecold)
        )
        TemperatureRange.VERY_COLD -> listOf(
            Outfit("Cold hands, warm heart, and a cozy winter jacket", "Stay warm in freezing temps with a winter coat, sweater, gloves, thick pants, wool socks, warm boots", R.drawable.cold3)
        )
        TemperatureRange.COLD -> listOf(
            Outfit("Cold hands, warm heart, and a cozy winter jacket", "Stay warm in freezing temps with a winter coat, sweater, gloves, thick pants, wool socks, warm boots", R.drawable.verycold)
        )
        TemperatureRange.CHILLY -> listOf(
            Outfit("Layer up! Cold weather is just an excuse for stylish outfits", "A good balance of warmth with a warm jacket, long sleeves, scarf, jeans, ankle boots", R.drawable.chilly)
        )
        TemperatureRange.COOL -> listOf(
            Outfit("A light jacket and a warm smile are all you need!", "A Light jacket, hoodie, jeans and sneakers are perfect for chilly days.", R.drawable.cool)
        )
        TemperatureRange.MILD -> listOf(
            Outfit("Sweater weather is better weather!", "Be Cozy and stylish with a Sweater, t-shirt, jeans, light sneakers.", R.drawable.mild)
        )
        TemperatureRange.WARM -> listOf(
            Outfit("The perfect balance – not too hot, not too cold!", "Want a simple and cool option? Wear a T-shirt, light sweater, jeans or chinos, sneakers.", R.drawable.warm)
        )
        TemperatureRange.HOT -> listOf(
            Outfit("Time to show off those summer vibes!", "A short-sleeve shirt, shorts, sandals or sneakers, sunglasses will keep you cool in warm weather.", R.drawable.hot)
        )
        TemperatureRange.VERY_HOT -> listOf(
            Outfit("Sun’s out, shades on, and light clothes all day!", "Stay cool in the heat with a tank top, cotton shorts, sandals, hat, sunglasses", R.drawable.veryhot)
        )

    }
}

