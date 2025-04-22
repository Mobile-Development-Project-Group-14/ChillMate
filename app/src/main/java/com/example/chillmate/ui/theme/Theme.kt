package com.example.chillmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppTheme {
    // Gradient Colors (used in HomeScreen background)
    val dayColors = listOf(
        Color(0xFF9A52C7),  // Light Purple
        Color(0xFF58126A),  // Medium Purple
        Color(0xFF4A1764)   // Dark Purple
    )

    val nightColors = listOf(
        Color(0xFF695A5A),  // Dark Brown
        Color(0xFF9F6060),  // Medium Brown
        Color(0xFF6B4F4F)   // Light Brown
    )

    // Component Colors
    val primaryButtonColor = Color(0xFF58126A)       // Purple button (added this)
    val secondaryButtonColor = Color(0x4D9A52C7)     // Transparent purple (added this)
    val transparentButtonColor = Color.White.copy(alpha = 0.2f)
    val buttonTextColor = Color.White


    // Text Colors
    val primaryTextColor = Color(0xFFFFFFFF)         // White
    val secondaryTextColor = Color(0xB3FFFFFF)       // Semi-transparent white
    val alertTextColor = Color(0xFFFFA500)           // Orange for alerts

    @Composable
    fun ChillMateTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colorScheme = if (darkTheme) {
            darkColorScheme(
                primary = nightColors[1],
                secondary = nightColors[2],
                surface = nightColors[0],
                onPrimary = primaryTextColor,
                onSurface = primaryTextColor
            )
        } else {
            lightColorScheme(
                primary = dayColors[0],
                secondary = dayColors[1],
                surface = dayColors[2],
                onPrimary = primaryTextColor,
                onSurface = primaryTextColor
            )
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

    // Helper functions
    fun getBackgroundGradient(isDay: Boolean): Brush {
        return Brush.verticalGradient(
            colors = if (isDay) dayColors else nightColors,
            startY = 0f,
            endY = 1000f
        )
    }

    @Composable
    fun getTransparentButtonColors() = ButtonDefaults.buttonColors(
        containerColor = transparentButtonColor,
        contentColor = buttonTextColor
    )

    @Composable
    fun getTopBarColor(isDay: Boolean): Color {
        return if (isDay) dayColors[2] else nightColors[1]
    }

    @Composable
    fun getCardColor(isDay: Boolean = isSystemInDarkTheme()): Color {
        return if (isDay) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f)
    }

    @Composable
    fun getTextColor(isDay: Boolean = isSystemInDarkTheme()): Color {
        return primaryTextColor
    }

    @Composable
    fun getSecondaryTextColor(isDay: Boolean = isSystemInDarkTheme()): Color {
        return secondaryTextColor
    }

    @Composable
    fun getButtonColor(isDay: Boolean): Color {
        return if (isDay) primaryButtonColor else nightColors[1]
    }

    @Composable
    fun getButtonTextColor(isDay: Boolean): Color {
        return primaryTextColor
    }

    @Composable
    fun getButtonColors(isDay: Boolean) = ButtonDefaults.buttonColors(
        containerColor = getButtonColor(isDay),
        contentColor = getButtonTextColor(isDay)
    )

    @Composable
    fun getSecondaryButtonColors() = ButtonDefaults.buttonColors(
        containerColor = secondaryButtonColor,
        contentColor = primaryTextColor
    )

    @Composable
    fun getAlertColors() = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = alertTextColor
    )
}