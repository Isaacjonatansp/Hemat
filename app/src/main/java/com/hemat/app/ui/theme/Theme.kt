package com.hemat.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF0F5132),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E7DD),
    onPrimaryContainer = Color(0xFF082E1C),
    secondary = Color(0xFF198754),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF0F3822),
    tertiary = Color(0xFFB45309),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF362000),
    background = Color(0xFFF4F7F5),
    onBackground = Color(0xFF101915),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101915),
    surfaceVariant = Color(0xFFE1EAE5),
    onSurfaceVariant = Color(0xFF405047),
    outline = Color(0xFF708378),
    error = Color(0xFFDC2626),
    onError = Color.White
)

private val Dark = darkColorScheme(
    primary = Color(0xFF41C588),
    onPrimary = Color(0xFF003822),
    primaryContainer = Color(0xFF0D3A25),
    onPrimaryContainer = Color(0xFFA3E6C5),
    secondary = Color(0xFF68D391),
    onSecondary = Color(0xFF003822),
    secondaryContainer = Color(0xFF1B4332),
    onSecondaryContainer = Color(0xFFB7F0CE),
    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF422000),
    tertiaryContainer = Color(0xFF362000),
    onTertiaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF0B120E),
    onBackground = Color(0xFFE1E8E4),
    surface = Color(0xFF121C17),
    onSurface = Color(0xFFE1E8E4),
    surfaceVariant = Color(0xFF1D2A24),
    onSurfaceVariant = Color(0xFFB3C2BA),
    outline = Color(0xFF82988C),
    error = Color(0xFFF87171),
    onError = Color(0xFF600000)
)

@Composable
fun HematTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (dark) Dark else Light,
        content = content
    )
}
