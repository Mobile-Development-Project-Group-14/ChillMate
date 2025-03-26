package com.example.chillmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppTheme {
    val dayColors = listOf(
        Color(0xFFC0DEFF),  // Light blue
        Color(0xFF74B6FF),  // Medium blue
        Color(0xFF419BFF)   // Dark blue
    )

    val nightColors = listOf(
        Color(0xFF695A5A), // Dark brown
        Color(0xFF9F6060), // Medium brown
        Color(0xFF6B4F4F)  // Light brown
    )

    fun getBackgroundGradient(isDay: Boolean): Brush {
        return Brush.verticalGradient(
            colors = if (isDay) dayColors else nightColors,
            startY = 0f,
            endY = 1000f
        )
    }

    fun getTopBarColor(isDay: Boolean): Color {
        return if (isDay) dayColors[2] else nightColors[1]
    }
}

@Composable
fun ChillMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
        typography = Typography,
        content = content
    )
}