
package com.example.weatherapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Coastal Weather - Light Theme
private val CoastalLightColorScheme = lightColorScheme(
    primary = CoastalDeepBlue,
    onPrimary = CoastalWhite,

    primaryContainer = CoastalMist,
    onPrimaryContainer = CoastalNavy,

    secondary = CoastalDeepBlue,
    onSecondary = CoastalWhite,

    secondaryContainer = CoastalMist,
    onSecondaryContainer = CoastalNavy,

    tertiary = CoastalSunshine,
    onTertiary = CoastalNavy,

    background = CoastalSky,
    onBackground = CoastalNavy,

    surface = CoastalWhite,
    onSurface = CoastalNavy,

    surfaceVariant = CoastalMist,
    onSurfaceVariant = CoastalSlate,

    surfaceContainer = CoastalWhite,
    surfaceContainerLow = CoastalWhite,
    surfaceContainerHigh = CoastalMist,

    outline = CoastalOutline
)

// Coastal Weather - Dark Theme
private val CoastalDarkColorScheme = darkColorScheme(
    primary = CoastalNightBlue,
    onPrimary = CoastalNight,

    primaryContainer = CoastalNightSurface,
    onPrimaryContainer = CoastalNightText,

    secondary = CoastalNightMuted,
    onSecondary = CoastalNight,

    secondaryContainer = CoastalNightSurface,
    onSecondaryContainer = CoastalNightText,

    tertiary = CoastalSunshine,
    onTertiary = CoastalNight,

    background = CoastalNight,
    onBackground = CoastalNightText,

    surface = CoastalNightSurface,
    onSurface = CoastalNightText,

    surfaceVariant = CoastalNightSurface,
    onSurfaceVariant = CoastalNightMuted,

    surfaceContainer = CoastalNightSurface,
    surfaceContainerLow = CoastalNightSurface,
    surfaceContainerHigh = CoastalNightSurface,

    outline = CoastalNightMuted
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> CoastalDarkColorScheme

        else -> CoastalLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
