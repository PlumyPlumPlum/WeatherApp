
package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.remote.WeatherApiClient
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

data class WeatherSnapshot(
    val current: CurrentWeather,
    val daily: List<ForecastDay>,
    val hourly: List<HourlyForecast>
)

class WeatherRepository
{
    private val api = WeatherApiClient.service

    suspend fun loadWeather(
        locationQuery: String
    ): WeatherSnapshot
    {
        val response = api.getForecast(
            key = BuildConfig.WEATHER_API_KEY,
            location = locationQuery,
            days = 3
        )

        val forecastDays = response.forecast.forecastday

        require(forecastDays.isNotEmpty()) {
            "The API returned no forecast days."
        }

        val zone = ZoneId.of(
            response.location.timeZoneId ?: "UTC"
        )

        val today = LocalDate.now(zone)

        val allHours = forecastDays
            .flatMap { it.hour }
            .sortedBy { it.timeEpoch }

        val currentHourStart =
            (Instant.now().epochSecond / 3600L) * 3600L

        val thisHour = allHours.firstOrNull {
            it.timeEpoch >= currentHourStart
        }

        fun precipitationType(
            rainChance: Int,
            snowChance: Int
        ): String
        {
            return if (snowChance > rainChance) {
                "Snow"
            } else {
                "Rain"
            }
        }

        val currentWeather = CurrentWeather(
            location = listOf(
                response.location.name,
                response.location.region
            )
                .filter { it.isNotBlank() }
                .joinToString(", "),

            temperatureC =
                response.current.temperatureC.roundToInt(),

            condition = response.current.condition.text,

            feelsLikeC =
                response.current.feelsLikeC.roundToInt(),

            highC = forecastDays.first()
                .day.highC.roundToInt(),

            lowC = forecastDays.first()
                .day.lowC.roundToInt(),

            windKph =
                response.current.windKph.roundToInt(),

            humidityPercent = response.current.humidity,

            windDirection = response.current.windDirection,

            precipitationMm = response.current.precipitationMm,

            precipitationChancePercent = thisHour?.let {
                maxOf(it.chanceOfRain, it.chanceOfSnow)
            },

            precipitationChanceType = thisHour?.let {
                precipitationType(
                    it.chanceOfRain,
                    it.chanceOfSnow
                )
            } ?: "Rain",

            conditionCode = response.current.condition.code
        )

        val dailyForecast = forecastDays.map { forecast ->
            val date = LocalDate.parse(forecast.date)

            ForecastDay(
                dayName = date.dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    Locale.CANADA
                ),

                dateLabel = (
                        if (date == today) "Today · " else ""
                        ) + date.format(
                    DateTimeFormatter.ofPattern(
                        "MMM d",
                        Locale.CANADA
                    )
                ),


                condition = forecast.day.condition.text,

                highC = forecast.day.highC.roundToInt(),
                lowC = forecast.day.lowC.roundToInt(),
                windKph = forecast.day.windKph.roundToInt(),
                humidityPercent = forecast.day.humidity,

                precipitationMm =
                    forecast.day.precipitationMm,

                precipitationChancePercent = maxOf(
                    forecast.day.chanceOfRain,
                    forecast.day.chanceOfSnow
                ),

                precipitationChanceType = precipitationType(
                    forecast.day.chanceOfRain,
                    forecast.day.chanceOfSnow
                ),

                conditionCode = forecast.day.condition.code
            )
        }

        val timeFormatter = DateTimeFormatter.ofPattern(
            "EEE h a",
            Locale.CANADA
        )

        val hourlyForecast = allHours
            .filter {
                it.timeEpoch >= currentHourStart
            }
            .take(24)
            .map { hour ->
                HourlyForecast(
                    timeLabel = Instant
                        .ofEpochSecond(hour.timeEpoch)
                        .atZone(zone)
                        .format(timeFormatter),

                    temperatureC =
                        hour.temperatureC.roundToInt(),

                    condition = hour.condition.text,
                    conditionCode = hour.condition.code,

                    precipitationMm = hour.precipitationMm,

                    rainChancePercent = hour.chanceOfRain,
                    snowChancePercent = hour.chanceOfSnow
                )
            }

        return WeatherSnapshot(
            current = currentWeather,
            daily = dailyForecast,
            hourly = hourlyForecast
        )
    }

    // Search for Canadian cities using WeatherAPI.
    suspend fun searchCanadianCities(
        query: String
    ): List<SavedLocation>
    {
        val results = api.searchCities(
            key = BuildConfig.WEATHER_API_KEY,
            query = query.trim()
        )

        return results
            .filter {
                it.country.equals(
                    "Canada",
                    ignoreCase = true
                )
            }
            .map {
                SavedLocation(
                    name = it.name,
                    region = it.region,
                    latitude = it.lat,
                    longitude = it.lon
                )
            }
    }
}
