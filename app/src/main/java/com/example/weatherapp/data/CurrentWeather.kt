package com.example.weatherapp.data

// data class to help move information around easier
data class CurrentWeather(
    val location: String,
    val temperatureC: Int,
    val condition: String,
    val feelsLikeC: Int,
    val highC: Int,
    val lowC: Int,
    val windKph: Int,
    val humidityPercent: Int
)