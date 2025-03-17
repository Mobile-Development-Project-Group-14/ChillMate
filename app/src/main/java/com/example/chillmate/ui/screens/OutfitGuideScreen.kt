package com.example.chillmate.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun OutfitGuideScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Center the content
    ) {
        Text(
            text = "Outfit Guide Screen",
            color = Color.Blue, // Set text color to blue
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold // Optional: Make text bold
        )
    }
}