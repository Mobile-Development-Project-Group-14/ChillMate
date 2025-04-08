package com.example.chillmate.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chillmate.ui.components.ChillMateScaffold
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel

data class Brand(val name: String, val url: String)

val clothingBrands = listOf(
    Brand("Reima", "https://www.reima.com/"),
    Brand("Polarn O. Pyret", "https://www.polarnopyret.fi/"),
    Brand("Gugguu", "https://www.gugguu.com/"),
    Brand("Metsola", "https://metsola.co/"),
    Brand("Papu Stories", "https://www.papustories.com/"),
    Brand("Mainio", "https://www.mainioclothing.com/")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(navController: NavController, viewModel: WeatherViewModel) {
    val isDay = when (val state = viewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true
    }

    val context = LocalContext.current

    ChillMateScaffold(
        navController = navController,
        isDay = isDay,
        title = "Shop Now",
        showBackButton = true
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.getBackgroundGradient(isDay))
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(clothingBrands) { brand ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(brand.url))
                                context.startActivity(intent)
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(brand.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(brand.url, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
