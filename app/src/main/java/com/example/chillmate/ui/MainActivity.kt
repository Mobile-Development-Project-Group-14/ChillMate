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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chillmate.ui.screens.HomeScreen
import com.example.chillmate.ui.screens.OutfitGuideScreen
import com.example.chillmate.ui.screens.TodayActivityScreen
import com.example.chillmate.ui.theme.ChillMateTheme
import com.google.android.gms.location.LocationServices


class MainActivity : ComponentActivity() {
    // Initialize the permission launcher
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChillMateTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ChillMate(locationPermissionLauncher)
                }
            }
        }
    }
}

@Composable
fun ChillMate(locationPermissionLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>) {
    val navController = rememberNavController()
    val locationState = remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val context = LocalContext.current

    // Check and request location permissions
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions already granted, fetch location
            fetchLocation(context) { lat, lon ->
                locationState.value = Pair(lat, lon)
            }
        } else {
            // Request permissions
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                location = locationState.value
            )
        }
        composable("outfitGuide") {
            OutfitGuideScreen()
        }
        composable("todayActivity") {
            TodayActivityScreen()
        }
    }
}

// Function to fetch location
private fun fetchLocation(context: Context, onLocationFetched: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationFetched(it.latitude, it.longitude)
            }
        }
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