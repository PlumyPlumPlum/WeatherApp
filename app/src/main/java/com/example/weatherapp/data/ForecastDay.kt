package com.example.weatherapp.data

import android.service.notification.Condition

data class ForecastDay(
    val dayName: String,
    val condition: String,
    val highC: Int,
    val lowC: Int,
    val windKph: Int,
    val humidityPercent: Int
)