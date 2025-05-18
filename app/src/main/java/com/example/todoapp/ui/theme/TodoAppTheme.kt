package com.example.todoapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlackGoldColorScheme = darkColorScheme(
    primary = Color(0xFFFFD700), // Gold
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF333333), // Dark gray for containers
    onPrimaryContainer = Color(0xFFFFD700), // Gold text on containers
    secondary = Color(0xFFFFB300), // Slightly darker gold
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun TodoAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BlackGoldColorScheme,
        typography = AppTypography,
        content = content
    )
}


