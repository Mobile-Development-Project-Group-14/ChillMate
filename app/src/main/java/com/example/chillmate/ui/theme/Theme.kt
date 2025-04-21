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
        Color(0xFF9A52C7), // Electric Violet
        Color(0xFF58126A), // Dusty Rose
        Color(0xFF4A1764)  // Soft Periwinkle


        // orange to blue gradient
        //  Color(0xFFFFE0B2),  // Light orange
        // Color(0xFFFFB74D),  // Medium orange
        // Color(0xFFFF9800),  // Dark orange
        //Color(0xFFC0DEFF),  // Light blue
        //Color(0xFF74B6FF),  // Medium blue
        //Color(0xFF419BFF)   // Dark blue


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

    fun getTextColor(day: Boolean): Color {
        return if (day) Color(0xFF000000) else Color(0xFFFFFFFF)
    }

    fun getButtonColor(day: Boolean): Color {
        return if (day) Color(0xFF58126A) else Color(0xFF6B4F4F)
    }

    fun getButtonTextColor(day: Boolean): Color {
        return if (day) Color(0xFF9E62C4) else Color(0xFF000000)
    }

    fun getCardColor(day: Boolean): Color {
        return if (day) Color(0xFF4A1764) else Color(0xFF9F6060)
    }

    fun getSecondaryTextColor(day: Boolean): Color {
        return if (day) Color(0xFF000000) else Color(0xFFB0B0B0)
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