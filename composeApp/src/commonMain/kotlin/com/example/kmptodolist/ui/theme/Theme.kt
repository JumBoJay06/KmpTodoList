package com.example.kmptodolist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue80,
    secondary = SecondaryGrey80,
    tertiary = TertiaryAccent80,
    background = BackgroundDark,
    error = Red
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue40,
    secondary = SecondaryGrey40,
    tertiary = TertiaryAccent40,
    background = BackgroundLight,
    error = Red
)

@Composable
fun KmpTodoListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}