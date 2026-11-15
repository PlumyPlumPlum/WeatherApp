
package com.example.weatherapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherSnapshot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weatherapp.data.LocationStore
import com.example.weatherapp.data.SavedLocation

// Describes the state that the UI can display.
data class WeatherUiState(
    val isLoading: Boolean = false,
    val weather: WeatherSnapshot? = null,
    val errorMessage: String? = null
)

data class CitySearchUiState(
    val isLoading: Boolean = false,
    val locations: List<SavedLocation> = emptyList(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

class WeatherViewModel(
application: Application
) : AndroidViewModel(application)
{
    private val repository = WeatherRepository()

    private val locationStore = LocationStore(
        application.applicationContext
    )

    // The city currently selected by the user.
    var selectedLocation by mutableStateOf(
        locationStore.loadSelected()
    )
        private set

    // All cities saved by the user.
    var savedLocations by mutableStateOf(
        locationStore.loadSaved()
    )
        private set

    // Last location name resolved from device GPS.
    var lastGpsName by mutableStateOf(
        locationStore.loadLastGpsName()
    )
        private set

    // Time of the last GPS location fix.
    var lastGpsTimestamp by mutableStateOf(
        locationStore.loadLastGpsTimestamp()
    )
        private set

    // Start with the saved city, or Halifax by default.
    private var locationQuery = selectedLocation.query

    private var loadingJob: Job? = null

    var uiState by mutableStateOf(WeatherUiState())
        private set

    private var searchJob: Job? = null

    var citySearchState by mutableStateOf(CitySearchUiState())
        private set

    init {
        loadWeather()
    }

    fun loadWeather(query: String = locationQuery)
    {
        locationQuery = query

        // Cancel an older request if a new one begins.
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            uiState = WeatherUiState(
                isLoading = true
            )

            try {
                val result = repository.loadWeather(query)

// If this response belongs to our GPS selection,
// remember the actual city identified by WeatherAPI.
                if (
                    selectedLocation.name == "My Location" &&
                    selectedLocation.query == query
                ) {
                    lastGpsName = result.current.location

                    locationStore.saveLastGpsInfo(
                        cityName = lastGpsName,
                        timestampMillis = lastGpsTimestamp
                    )
                }

                uiState = WeatherUiState(
                    weather = result
                )

            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                uiState = WeatherUiState(
                    errorMessage = e.message
                        ?: "Unable to retrieve weather."
                )
            }
        }
    }

    fun retry()
    {
        loadWeather()
    }

    // Select a city and retrieve its weather.
    fun selectLocation(location: SavedLocation)
    {
        selectedLocation = location

        locationStore.saveSelected(location)

        loadWeather(location.query)
    }

    // Select a location obtained from the device's GPS.
    fun selectDeviceLocation(
        location: SavedLocation,
        timestampMillis: Long
    )
    {
        lastGpsName = null
        lastGpsTimestamp = timestampMillis

        locationStore.saveLastGpsInfo(
            cityName = null,
            timestampMillis = timestampMillis
        )

        selectLocation(location)
    }

    // Add a city to saved locations.
    fun saveLocation(location: SavedLocation)
    {
        val updatedLocations = (savedLocations + location)
            .distinctBy { it.query }

        savedLocations = updatedLocations

        locationStore.saveLocations(updatedLocations)
    }

    // Remove a city from saved locations.
    fun removeLocation(location: SavedLocation)
    {
        val updatedLocations = savedLocations.filterNot {
            it.query == location.query
        }

        savedLocations = updatedLocations

        locationStore.saveLocations(updatedLocations)
    }

    // Search WeatherAPI for Canadian cities.
    fun searchCanadianCities(searchText: String)
    {
        val query = searchText.trim()

        // Cancel any previous search.
        searchJob?.cancel()

        if (query.length < 2) {
            citySearchState = CitySearchUiState(
                errorMessage = "Enter at least two characters."
            )
            return
        }

        searchJob = viewModelScope.launch {
            citySearchState = CitySearchUiState(
                isLoading = true
            )

            try {
                val results = repository.searchCanadianCities(
                    query
                )

                citySearchState = CitySearchUiState(
                    locations = results.distinctBy { it.query },
                    hasSearched = true
                )
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                citySearchState = CitySearchUiState(
                    errorMessage = "Unable to search cities. Please try again.",
                    hasSearched = true
                )
            }
        }
    }


}
