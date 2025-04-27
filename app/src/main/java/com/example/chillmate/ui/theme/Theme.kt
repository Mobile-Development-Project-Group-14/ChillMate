package com.example.chillmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppTheme {
    // Gradient Colors (used in HomeScreen background)
    val dayColors = listOf(
        Color(0xFFE0F7FA),  // Light Cyan – soft, airy background (like clear sky)
        Color(0xFF4FC3F7),  // Sky Blue – primary element color
        Color(0xFF0288D1)   // Deep Sky Blue – headers or stronger contrast elements
    )

    val nightColors = listOf(
        Color(0xFF695A5A),  // Dark Brown
        Color(0xFF9F6060),  // Medium Brown
        Color(0xFF6B4F4F)   // Light Brown
    )

    // Component Colors
    val transparentButtonColor = Color.White.copy(alpha = 0.2f)

    // Text Colors
    val primaryTextColor = Color(0xFFFFFFFF)         // White
    val secondaryTextColor = Color(0xB3FFFFFF)       // Semi-transparent white


    // colors for alerts
    val lightAlertTextColor = Color(0xFFEAEEEB)  // SaddleBrown for light mode
    val darkAlertTextColor = Color(0xFFF1EEE9)   // Orange for dark mode

    // calendar backgrounds
    val lightCalendarBackground = Color(0xFF3D3B3B)  // Light gray background
    val darkCalendarBackground = Color(0xFF1E1E1E)  // Dark gray background
    val calendarHeaderColor = Color(0xFF58126A)     // Your purple accent color
    val calendarSelectedColor = Color(0xFF7D3C98)

    @Composable
    fun getAlertTextColor(isDay: Boolean): Color {
        return if (isDay) lightAlertTextColor else darkAlertTextColor
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun getCalendarColors(isDay: Boolean): DatePickerColors {
        return DatePickerDefaults.colors(
            containerColor = if (isDay) lightCalendarBackground else darkCalendarBackground,
            titleContentColor = if (isDay) Color.Black else Color.White,
            headlineContentColor = if (isDay) Color.Black else Color.White,
            weekdayContentColor = if (isDay) Color(0xFF0288D1) else calendarHeaderColor, // Use your dayColors[2] for day mode
            dayContentColor = if (isDay) Color.Black else Color.White,
            disabledDayContentColor = if (isDay) Color.LightGray else Color.DarkGray,
            selectedDayContainerColor = if (isDay) Color(0xFF4FC3F7) else calendarSelectedColor, // Use your dayColors[1] for day mode
            selectedDayContentColor = Color.White,
            todayContentColor = if (isDay) Color(0xFF0288D1) else calendarHeaderColor, // Use your dayColors[2] for day mode
            todayDateBorderColor = if (isDay) Color(0xFF0288D1) else calendarHeaderColor, // Use your dayColors[2] for day mode
            dividerColor = if (isDay) Color.LightGray.copy(alpha = 0.5f) else calendarHeaderColor.copy(alpha = 0.5f)
        )
    }

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
    fun getTopBarColor(isDay: Boolean): Color {
        return if (isDay) dayColors[2] else nightColors[1]
    }

    @Composable
    fun getCardColor(isDay: Boolean = isSystemInDarkTheme()): Color {
        return if (isDay) Color(0xFFF3E0D2).copy(alpha = 0.9f)  // Warm linen
        else Color(0xFF7A5C50).copy(alpha = 0.8f)       // Muted brown
    }

    @Composable
    fun getTextColor(isDay: Boolean = isSystemInDarkTheme()): Color {
        return if (isDay) Color(0xFF5C4B3A)  // Deep taupe (readable on linen)
        else Color(0xFFF5E6D2)        // Cream (soft on brown)
    }

    @Composable
    fun getSecondaryTextColor(isDay: Boolean = isSystemInDarkTheme()): Color {
        return if (isDay) Color(0xFF7A6A5A)  // Muted taupe
        else Color(0xFFD9C7B8)         // Warm gray
    }

    @Composable
    fun getButtonColor(isDay: Boolean): Color {
        return if (isDay) Color.White.copy(alpha = 0.3f)  // Rich terracotta (day)
        else Color.White.copy(alpha = 0.3f)        // Medium brown (night)
    }

    @Composable
    fun getButtonTextColor(isDay: Boolean): Color {
        return if (isDay) Color(0xFFFFFFFF)  // White (contrasts with terracotta)
        else Color(0xFFF5E6D2)        // Cream (soft on brown)
    }

}