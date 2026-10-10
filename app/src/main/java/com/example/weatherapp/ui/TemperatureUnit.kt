
package com.example.weatherapp.ui

import kotlin.math.roundToInt

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}

fun formatTemperature(
    celsius: Int,
    unit: TemperatureUnit
): String {
    return when (unit) {
        TemperatureUnit.CELSIUS -> {
            "${celsius}°C"
        }

        TemperatureUnit.FAHRENHEIT -> {
            val fahrenheit = (celsius * 9.0 / 5.0 + 32).roundToInt()
            "${fahrenheit}°F"
        }
    }
}
