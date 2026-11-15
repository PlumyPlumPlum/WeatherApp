
package com.example.weatherapp.data

data class HourlyForecast(
    val timeLabel: String,
    val temperatureC: Int,
    val condition: String,
    val conditionCode: Int,
    val precipitationMm: Double,
    val rainChancePercent: Int,
    val snowChancePercent: Int
)
