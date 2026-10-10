package com.example.weatherapp.data

object SampleWeatherData {

    // placeholder data | one day
    val currentWeather = CurrentWeather(
        location = "Halifax, Nova Scotia",
        temperatureC = 18,
        condition = "Partly Cloudy",
        feelsLikeC = 17,
        highC = 21,
        lowC = 12,
        windKph = 14,
        humidityPercent = 64
    )

    // static list for demo purposes | many days
    val dailyForecast = listOf(
        ForecastDay(
            dayName = "Today",
            condition = "Partly Cloudy",
            highC = 21,
            lowC = 12,
            windKph = 14,
            humidityPercent = 64
        ),
        ForecastDay(
            dayName = "Friday",
            condition = "Light Rain",
            highC = 17,
            lowC = 10,
            windKph = 18,
            humidityPercent = 82
        ),
        ForecastDay(
            dayName = "Saturday",
            condition = "Sunny",
            highC = 19,
            lowC = 9,
            windKph = 11,
            humidityPercent = 58
        ),
        ForecastDay(
            dayName = "Sunday",
            condition = "Cloudy",
            highC = 16,
            lowC = 8,
            windKph = 16,
            humidityPercent = 69
        ),
        ForecastDay(
            dayName = "Monday",
            condition = "Rain",
            highC = 14,
            lowC = 7,
            windKph = 22,
            humidityPercent = 86
        ),
        ForecastDay(
            dayName = "Tuesday",
            condition = "Sunny",
            highC = 18,
            lowC = 9,
            windKph = 10,
            humidityPercent = 55
        ),
        ForecastDay(
            dayName = "Wednesday",
            condition = "Partly Cloudy",
            highC = 20,
            lowC = 11,
            windKph = 13,
            humidityPercent = 61
        )
    )
}