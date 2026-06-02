package com.example.pr24.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkCream,
    onPrimary = DarkCoffee,
    secondary = Honey,
    tertiary = Berry,
    background = DarkCoffee,
    onBackground = DarkCream,
    surface = DarkSurface,
    onSurface = DarkCream,
    surfaceVariant = Color(0xFF443832),
    onSurfaceVariant = Color(0xFFE7D5C9)
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    onPrimary = Color.White,
    secondary = Sage,
    onSecondary = Color.White,
    tertiary = Berry,
    background = CoffeeCream,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF0E2D6),
    onSurfaceVariant = Color(0xFF5C4B42),
    primaryContainer = Color(0xFFE9D1BF),
    onPrimaryContainer = Espresso,
    secondaryContainer = Color(0xFFDCE8D7),
    onSecondaryContainer = Color(0xFF243626)
)

@Composable
fun Pr24Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
