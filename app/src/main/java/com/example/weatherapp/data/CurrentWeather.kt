package com.example.weatherapp.data

data class CurrentWeather(
    val location: String,
    val temperatureC: Int,
    val condition: String,
    val feelsLikeC: Int,
    val highC: Int,
    val lowC: Int,
    val windKph: Int,
    val humidityPercent: Int,

    val windDirection: String = "",
    val precipitationMm: Double = 0.0,
    val precipitationChancePercent: Int? = null,
    val precipitationChanceType: String = "Rain",
    val conditionCode: Int? = null
)
