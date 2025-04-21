package com.example.chillmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chillmate.model.Activity
import com.example.chillmate.ui.theme.AppTheme
import com.example.chillmate.viewmodel.WeatherUiState
import com.example.chillmate.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    navController: NavController,
    viewModel: WeatherViewModel = viewModel(),
    activity: Activity?


) {
    val isDay = when (val state = viewModel.weatherUiState) {
        is WeatherUiState.Success -> state.data.current.is_day == 1
        else -> true // Default to day theme if no data
    }

    val backgroundGradient = AppTheme.getBackgroundGradient(isDay)


    if (activity == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Activity data not available",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        activity.name,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
        ) {
            ActivityDetailContent(
                activity = activity,
                isDay = isDay,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun ActivityDetailContent(
    activity: Activity,
    isDay: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(2f / 3f)
        )
        {
        Image(
            painter = painterResource(id = activity.imageRes),
            contentDescription = "Activity image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
        )
        }

        // Content section
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Basic info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    icon = Icons.Default.Info,
                    text = activity.duration ?: "Flexible"
                )
                PriceLevelChip(level = activity.priceLevel)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Detailed description
            Text(
                text = activity.detailedDescription,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Equipment section
            if (activity.equipment.isNotEmpty()) {
                DetailSection(
                    icon = Icons.Default.Build,
                    title = "Equipment Needed",
                    items = activity.equipment
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Requirements section
            if (activity.requirements.isNotEmpty()) {
                DetailSection(
                    icon = Icons.Default.Check,
                    title = "Requirements",
                    items = activity.requirements
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Best time section
            activity.bestTime?.let {
                DetailSection(
                    icon = Icons.Default.PlayArrow,
                    title = "Best Time",
                    items = listOf(it)
                )
            }
        }
    }
}

// Reusable components remain the same as previous version
@Composable
private fun DetailSection(
    icon: ImageVector,
    title: String,
    items: List<String>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
        Column(modifier = Modifier.padding(start = 28.dp)) {
            items.forEach { item ->
                Text(
                    text = "• $item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

@Composable
private fun PriceLevelChip(level: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "★".repeat(level) + "☆".repeat(3 - level),
            color = Color.Yellow,
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = when (level) {
                0 -> "Free"
                1 -> "€"
                2 -> "€€"
                3 -> "€€€"
                else -> ""
            },
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}