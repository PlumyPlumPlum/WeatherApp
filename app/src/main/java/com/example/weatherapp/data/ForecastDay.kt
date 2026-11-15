package com.example.weatherapp.data

data class ForecastDay(
    val dayName: String,
    val condition: String,
    val highC: Int,
    val lowC: Int,
    val windKph: Int,
    val humidityPercent: Int,

    val dateLabel: String = "",
    val precipitationMm: Double = 0.0,
    val precipitationChancePercent: Int = 0,
    val precipitationChanceType: String = "Rain",
    val conditionCode: Int? = null
)
