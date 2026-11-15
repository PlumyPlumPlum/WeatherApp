
package com.example.weatherapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocationStore(context: Context)
{
    private val preferences = context.getSharedPreferences(
        "weather_locations",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    // Retrieve the last selected location.
    fun loadSelected(): SavedLocation
    {
        val json = preferences.getString(
            "selected_location",
            null
        )

        // Default to Halifax if no location is saved.
        val defaultLocation = SavedLocation(
            name = "Halifax",
            region = "Nova Scotia",
            latitude = 44.6488,
            longitude = -63.5752
        )

        if (json == null) {
            return defaultLocation
        }

        return runCatching {
            gson.fromJson(json, SavedLocation::class.java)
                ?: defaultLocation
        }.getOrDefault(defaultLocation)
    }

    // Remember which city the user selected.
    fun saveSelected(location: SavedLocation)
    {
        preferences.edit()
            .putString(
                "selected_location",
                gson.toJson(location)
            )
            .apply()
    }

    // Load the user's saved cities.
    fun loadSaved(): List<SavedLocation>
    {
        val json = preferences.getString(
            "saved_locations",
            null
        ) ?: return emptyList()

        val type = object :
            TypeToken<List<SavedLocation>>() {}.type

        return runCatching {
            gson.fromJson<List<SavedLocation>>(
                json,
                type
            ) ?: emptyList()
        }.getOrDefault(emptyList())
    }

    // Persist the saved city list.
    fun saveLocations(locations: List<SavedLocation>)
    {
        preferences.edit()
            .putString(
                "saved_locations",
                gson.toJson(locations)
            )
            .apply()
    }

    // Retrieve the last city detected through GPS.
    fun loadLastGpsName(): String?
    {
        return preferences.getString(
            "last_gps_city",
            null
        )
    }

    // Retrieve when the last GPS fix was obtained.
    fun loadLastGpsTimestamp(): Long
    {
        return preferences.getLong(
            "last_gps_fix_time",
            0L
        )
    }

    // Remember the last detected city and GPS fix time.
    fun saveLastGpsInfo(
        cityName: String?,
        timestampMillis: Long
    )
    {
        preferences.edit()
            .putString("last_gps_city", cityName)
            .putLong("last_gps_fix_time", timestampMillis)
            .apply()
    }
}
