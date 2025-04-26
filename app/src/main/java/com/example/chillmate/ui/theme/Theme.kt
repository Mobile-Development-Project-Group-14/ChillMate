package com.example.chillmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
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
        Color(0xFFF8EDE3),  // Linen white
        Color(0xFFE8A87C),  // Terracotta (straps/heat waves)
        Color(0xFFA38B6D)   // Sandstone (cobblestones)
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

    // colors for alerts
    val lightAlertTextColor = Color(0xFF8B4513)  // SaddleBrown for light mode
    val darkAlertTextColor = Color(0xFFFFA500)   // Orange for dark mode

    // calendar backgrounds
    val lightCalendarBackground = Color(0xFF1A1A1A)  // Light gray background
    val darkCalendarBackground = Color(0xFF1A1A1A)  // Dark gray background
    val calendarHeaderColor = Color(0xFF58126A)     // Your purple accent color
    val calendarSelectedColor = Color(0xFF7D3C98)

    @Composable
    fun getAlertTextColor(isDay: Boolean): Color {
        return if (isDay) lightAlertTextColor else darkAlertTextColor
    }

    @Composable
    fun getCalendarBackground(isDay: Boolean): Color {
        return if (isDay) lightCalendarBackground else darkCalendarBackground
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun getCalendarColors(isDay: Boolean): DatePickerColors {
        return DatePickerDefaults.colors(
            containerColor = if (isDay) lightCalendarBackground else darkCalendarBackground,
            titleContentColor = if (isDay) Color.Black else Color.White,
            headlineContentColor = if (isDay) Color.Black else Color.White,
            weekdayContentColor = calendarHeaderColor,
            dayContentColor = if (isDay) Color.Black else Color.White,
            disabledDayContentColor = if (isDay) Color.LightGray else Color.DarkGray,
            selectedDayContainerColor = calendarSelectedColor,
            selectedDayContentColor = Color.White,
            todayContentColor = calendarHeaderColor,
            todayDateBorderColor = calendarHeaderColor,
            dividerColor = calendarHeaderColor.copy(alpha = 0.5f)
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
        return if (isDay) Color(0xFFD49A6A)  // Rich terracotta (day)
        else Color(0xFF9F6060)        // Medium brown (night)
    }

    @Composable
    fun getButtonTextColor(isDay: Boolean): Color {
        return if (isDay) Color(0xFFFFFFFF)  // White (contrasts with terracotta)
        else Color(0xFFF5E6D2)        // Cream (soft on brown)
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
    fun getAlertColors(isDay: Boolean) = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = getAlertTextColor(isDay)
    )
}