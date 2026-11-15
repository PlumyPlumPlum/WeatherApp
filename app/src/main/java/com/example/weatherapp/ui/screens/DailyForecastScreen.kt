
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
import java.util.Locale
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider

@Composable
fun DailyForecastScreen(
    forecast: List<ForecastDay>,
    location: String = "Halifax, Nova Scotia",
    unit: TemperatureUnit = TemperatureUnit.CELSIUS,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = location,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "3-Day Forecast",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Day and condition

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Primary heading - day of the week
                    Text(
                        text = forecastDay.dayName.uppercase(Locale.CANADA),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Secondary heading - date
                    Text(
                        text = forecastDay.dateLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Weather condition
                    Text(
                        text = forecastDay.condition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }


                // Weather illustration
                WeatherIcon(
                    condition = forecastDay.condition,
                    modifier = Modifier.size(44.dp),
                    code = forecastDay.conditionCode
                )

                // High and low temperatures

// High and low temperatures
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "HIGH",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = formatTemperature(forecastDay.highC, unit),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Low ${formatTemperature(forecastDay.lowC, unit)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }

            // Additional forecast details

// Subtle separation between forecast and measurements
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(
                    alpha = 0.5f
                )
            )

// First measurement row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ForecastMetric(
                    label = "WIND",
                    value = "${forecastDay.windKph} km/h",
                    modifier = Modifier.weight(1f)
                )

                ForecastMetric(
                    label = "HUMIDITY",
                    value = "${forecastDay.humidityPercent}%",
                    modifier = Modifier.weight(1f)
                )
            }

// Second measurement row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ForecastMetric(
                    label = "${forecastDay.precipitationChanceType.uppercase(Locale.CANADA)} CHANCE",
                    value = "${forecastDay.precipitationChancePercent}%",
                    modifier = Modifier.weight(1f)
                )

                ForecastMetric(
                    label = "PRECIPITATION",
                    value = String.format(
                        Locale.CANADA,
                        "%.1f mm",
                        forecastDay.precipitationMm
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DailyForecastPreview() {
    WeatherAppTheme {
        DailyForecastScreen(
            forecast = SampleWeatherData.dailyForecast.take(3)
        )
    }
}

@Composable
private fun ForecastMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

