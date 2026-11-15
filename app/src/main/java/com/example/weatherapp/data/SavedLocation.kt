package com.example.weatherapp.data

data class SavedLocation(
    val name: String,
    val region: String,
    val latitude: Double,
    val longitude: Double
)
{
    val query: String
        get() = "$latitude,$longitude"

    val displayName: String
        get() = listOf(name, region)
            .filter { it.isNotBlank() }
            .joinToString(", ")
}
