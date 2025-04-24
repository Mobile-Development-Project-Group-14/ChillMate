package com.example.chillmate.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chillmate.R
import com.example.chillmate.ui.components.ChillMateScaffold
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel

data class ClothingBrand(
    val name: String,
    val imageResId: Int,
    val url: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(navController: NavController, viewModel: WeatherViewModel) {
    val isDay = when (val state = viewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true
    }

    val context = LocalContext.current

    val brands = listOf(
        ClothingBrand("H&M", R.drawable.h_and_m, "https://www2.hm.com/", "To make fashion accessible and enjoyable for all"),
        ClothingBrand("Zalando", R.drawable.zalando, "https://www.zalando.com/", "Free your fashion"),
        ClothingBrand("UNIQLO", R.drawable.uniqlo, "https://www.uniqlo.com/", "Made for all"),
        ClothingBrand("ZARA", R.drawable.zara, "https://www.zara.com/", "Bringing elegance to everyday fashion"),
        ClothingBrand("ASOS", R.drawable.asos, "https://www.asos.com/", "Discover fashion online"),
        ClothingBrand("Reima", R.drawable.reima, "https://www.reima.com/", "Gear up kids for every weather"),
        ClothingBrand("New Yorker", R.drawable.newyorker, "https://www.newyorker.de/", "Dress for the moment"),
        ClothingBrand("Lindex", R.drawable.lindex, "https://www.lindex.com/", "Empowering women through fashion"),
        ClothingBrand("Cubus", R.drawable.cubus, "https://www.cubus.com/", "Affordable everyday fashion"),
        ClothingBrand("Bik Bok", R.drawable.bikbok, "https://www.bikbok.com/", "Trend-forward clothing for girls")
    )

    ChillMateScaffold(
        navController = navController,
        isDay = isDay,
        title = "Shop Now",
        showBackButton = true
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.getBackgroundGradient(isDay))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Explore Brands",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.getTextColor(isDay),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(brands.size) { index ->
                    val brand = brands[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(brand.url))
                                context.startActivity(intent)
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = brand.imageResId),
                                contentDescription = brand.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .padding(bottom = 12.dp)
                            )

                            Text(
                                text = brand.description,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.getSecondaryTextColor(isDay),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                }
            }
        }
    }
}
