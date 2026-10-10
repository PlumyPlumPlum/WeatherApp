
package com.example.weatherapp.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.SampleWeatherData
import com.example.weatherapp.ui.components.WeatherIcon
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.TemperatureUnit
import com.example.weatherapp.ui.formatTemperature


@Composable
fun CurrentWeatherScreen(
    weather: CurrentWeather,
    unit: TemperatureUnit = TemperatureUnit.CELSIUS,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Location
        Text(
            text = weather.location,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Main weather illustration
        WeatherIcon(
            condition = weather.condition,
            modifier = Modifier.size(112.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Current temperature
        Text(
            text = formatTemperature(weather.temperatureC, unit),
            fontSize = 68.sp,
            fontWeight = FontWeight.Bold
        )

        // Weather condition
        Text(
            text = weather.condition,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Feels-like temperature
        Text(
            text = "Feels like ${formatTemperature(weather.feelsLikeC, unit)}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // High and low temperatures
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                label = "High",
                value = formatTemperature(weather.highC, unit),
                modifier = Modifier.weight(1f)
            )

            WeatherDetailCard(
                label = "Low",
                value = formatTemperature(weather.lowC, unit),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Wind and humidity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeatherDetailCard(
                label = "Wind",
                value = "${weather.windKph} km/h",
                modifier = Modifier.weight(1f)
            )

            WeatherDetailCard(
                label = "Humidity",
                value = "${weather.humidityPercent}%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun WeatherDetailCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPreview() {
    WeatherAppTheme {
        CurrentWeatherScreen(
            weather = SampleWeatherData.currentWeather
        )
    }
}
