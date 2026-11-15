
package com.example.weatherapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.weatherapp.R

@Composable
fun WeatherIcon(
    condition: String,
    modifier: Modifier = Modifier,
    code: Int? = null
) {
    val iconId = when (code) {

        // Clear and cloudy
        1000 -> R.drawable.ic_weather_sunny
        1003 -> R.drawable.ic_weather_partly_cloudy
        1006 -> R.drawable.ic_weather_cloudy
        1009 -> R.drawable.ic_weather_overcast

        // Patchy and light rain
        1063, 1150, 1153, 1180, 1183, 1240 ->
            R.drawable.ic_weather_light_rain

        // Moderate rain
        1186, 1189, 1243 ->
            R.drawable.ic_weather_rain

        // Heavy rain
        1192, 1195, 1246, 1273, 1276 ->
            R.drawable.ic_weather_heavy_rain

        // Snow
        1066, 1114, 1117,
        1210, 1213, 1216, 1219,
        1222, 1225, 1255, 1258,
        1279, 1282 ->
            R.drawable.ic_weather_snow

        // Sleet and freezing precipitation
        1069, 1072, 1168, 1171,
        1198, 1201, 1204, 1207,
        1237, 1249, 1252, 1261, 1264 ->
            R.drawable.ic_weather_sleet

        // Mist and fog
        1030, 1135, 1147 ->
            R.drawable.ic_weather_cloudy

        // Fallback for other codes
        else -> {
            val text = condition.lowercase()

            when {
                "snow" in text ->
                    R.drawable.ic_weather_snow

                "sleet" in text || "freezing" in text ->
                    R.drawable.ic_weather_sleet

                "heavy rain" in text ->
                    R.drawable.ic_weather_heavy_rain

                "rain" in text || "drizzle" in text ->
                    R.drawable.ic_weather_light_rain

                "partly cloudy" in text ->
                    R.drawable.ic_weather_partly_cloudy

                "sunny" in text || "clear" in text ->
                    R.drawable.ic_weather_sunny

                else -> R.drawable.ic_weather_cloudy
            }
        }
    }

    Image(
        painter = painterResource(id = iconId),
        contentDescription = condition,
        modifier = modifier
    )
}
