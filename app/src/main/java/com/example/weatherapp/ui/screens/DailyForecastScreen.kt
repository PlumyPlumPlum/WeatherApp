
package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.ForecastDay
import com.example.weatherapp.data.SampleWeatherData
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import com.example.weatherapp.ui.components.WeatherIcon
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import com.example.weatherapp.ui.TemperatureUnit
import com.example.weatherapp.ui.formatTemperature

@Composable
fun DailyForecastScreen(
    forecast: List<ForecastDay>,
    unit: TemperatureUnit = TemperatureUnit.CELSIUS,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Halifax, Nova Scotia",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "7-Day Forecast",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 16.dp
            )
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forecast) { day ->
                ForecastCard(
                    forecastDay = day,
                    unit = unit
                )
            }
        }
    }
}


@Composable
fun ForecastCard(
    forecastDay: ForecastDay,
    unit: TemperatureUnit = TemperatureUnit.CELSIUS,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Day and condition
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = forecastDay.dayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = forecastDay.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Weather illustration
                WeatherIcon(
                    condition = forecastDay.condition,
                    modifier = Modifier.size(44.dp)
                )

                // High and low temperatures
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "High ${formatTemperature(forecastDay.highC, unit)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Low ${formatTemperature(forecastDay.lowC, unit)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Additional forecast details
            Text(
                text = "Wind ${forecastDay.windKph} km/h  ·  Humidity ${forecastDay.humidityPercent}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyForecastPreview() {
    WeatherAppTheme {
        DailyForecastScreen(
            forecast = SampleWeatherData.dailyForecast
        )
    }
}
