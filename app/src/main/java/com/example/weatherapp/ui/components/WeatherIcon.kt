
package com.example.weatherapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.weatherapp.R

@Composable
fun WeatherIcon(
    condition: String,
    modifier: Modifier = Modifier
) {
    val iconId = when (condition.trim().lowercase()) {
        "sunny" -> R.drawable.ic_weather_sunny
        "partly cloudy" -> R.drawable.ic_weather_partly_cloudy
        "cloudy" -> R.drawable.ic_weather_cloudy
        "overcast" -> R.drawable.ic_weather_overcast
        "light rain" -> R.drawable.ic_weather_light_rain
        "rain" -> R.drawable.ic_weather_rain
        "heavy rain" -> R.drawable.ic_weather_heavy_rain
        else -> R.drawable.ic_weather_cloudy
    }

    Image(
        painter = painterResource(id = iconId),
        contentDescription = condition,
        modifier = modifier
    )
}
