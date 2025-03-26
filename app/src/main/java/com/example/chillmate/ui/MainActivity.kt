package com.example.chillmate.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chillmate.ui.screens.HomeScreen
import com.example.chillmate.ui.screens.OutfitGuideScreen
import com.example.chillmate.ui.screens.TodayActivityScreen
import com.example.chillmate.ui.theme.ChillMateTheme
import com.example.chillmate.viewmodel.WeatherViewModel
import com.google.android.gms.location.LocationServices


class MainActivity : ComponentActivity() {
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // Handle permission granted
        } else {
            // Handle permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChillMateTheme {
                ChillMateApp(locationPermissionLauncher)
            }
        }
    }
}

@Composable
fun ChillMateApp(locationPermissionLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>) {
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocation(context, viewModel)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            viewModel.updateLocation(null)
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("outfitGuide") {
            OutfitGuideScreen(navController = navController, weatherViewModel = viewModel)

        }
        composable("todayActivity") {
            TodayActivityScreen(navController=navController, viewModel=viewModel)
        }
    }
}

private fun fetchLocation(
    context: Context,
    viewModel: WeatherViewModel
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                viewModel.updateLocation(Pair(it.latitude, it.longitude))
            } ?: run {
                viewModel.updateLocation(null)
            }
        }.addOnFailureListener {
            viewModel.updateLocation(null)
        }
    } catch (e: SecurityException) {
        viewModel.updateLocation(null)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Failed to load weather data", color = MaterialTheme.colorScheme.error)
    }
}