package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.SavedLocation
import com.example.weatherapp.ui.TemperatureUnit
import com.example.weatherapp.ui.components.WeatherBottomNavigation
import com.example.weatherapp.ui.screens.CurrentWeatherScreen
import com.example.weatherapp.ui.screens.DailyForecastScreen
import com.example.weatherapp.ui.screens.LocationsScreen
import com.example.weatherapp.ui.screens.LocationMapScreen
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.mapbox.common.location.DeviceLocationProviderType
import com.mapbox.common.location.LocationObserver
import com.mapbox.common.location.LocationServiceFactory
import com.mapbox.common.location.Location as MapboxLocation
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    var selectedUnit by rememberSaveable {
        mutableStateOf(TemperatureUnit.CELSIUS)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val selectedIndex = if (currentRoute == "daily") 1 else 0

    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState = weatherViewModel.uiState

    val context = LocalContext.current

    var startupLocationRequested by rememberSaveable {
        mutableStateOf(false)
    }

    val mapboxProvider = remember {
        val result = LocationServiceFactory.getOrCreate()
            .getDeviceLocationProvider(
                DeviceLocationProviderType.ANDROID
            )

        if (result.isValue) result.value else null
    }

    var activeLocationObserver by remember {
        mutableStateOf<LocationObserver?>(null)
    }

    DisposableEffect(mapboxProvider) {
        onDispose {
            activeLocationObserver?.let { observer ->
                mapboxProvider?.removeLocationObserver(observer)
            }
        }
    }

    // Is the phone currently retrieving its location?
    var isLocating by remember {
        mutableStateOf(false)
    }

    // Any error relating specifically to GPS.
    var locationError by remember {
        mutableStateOf<String?>(null)
    }

    fun hasLocationPermission(): Boolean
    {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    @SuppressLint("MissingPermission")
    fun fetchDeviceLocation() {
        if (!hasLocationPermission()) {
            locationError = "Location permission is required."
            return
        }

        val provider = mapboxProvider ?: run {
            locationError = "Location provider unavailable."
            return
        }

        if (isLocating) return

        isLocating = true
        locationError = null

        val observer = object : LocationObserver {
            override fun onLocationUpdateReceived(
                locations: List<MapboxLocation>
            ) {
                val location = locations.lastOrNull()
                    ?: return

                activeLocationObserver?.let {
                    provider.removeLocationObserver(it)
                }
                activeLocationObserver = null
                isLocating = false

                val myLocation = SavedLocation(
                    name = "My Location",
                    region = "",
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                // Save the GPS timestamp and coordinates for the map and report.
                weatherViewModel.selectDeviceLocation(
                    myLocation,
                    System.currentTimeMillis()
                )
            }
        }

        activeLocationObserver = observer

        try {
            provider.addLocationObserver(observer)
        } catch (e: SecurityException) {
            activeLocationObserver = null
            isLocating = false
            locationError = "Location permission unavailable."
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val coarseGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (fineGranted || coarseGranted) {
                fetchDeviceLocation()
            } else {
                isLocating = false

                locationError =
                    "Location permission denied. " +
                            "You can still search for cities manually."
            }
        }

    fun requestMyLocation()
    {
        if (isLocating) {
            return
        }

        locationError = null

        if (hasLocationPermission()) {

            // Permission already granted.
            fetchDeviceLocation()

        } else {

            // Ask for permission.
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        if (!startupLocationRequested) {
            startupLocationRequested = true
            requestMyLocation()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            currentRoute == "locations" -> {
                                "Locations"
                            }

                            currentRoute == "map" -> {
                                "Device Location Map"
                            }

                            weatherViewModel.selectedLocation.name == "My Location" -> {
                                weatherState.weather?.current?.location
                                    ?.substringBefore(",")
                                    ?: "My Location"
                            }

                            else -> {
                                weatherViewModel.selectedLocation.name
                            }
                        },
                    )
                },

                navigationIcon = {
                    if (currentRoute == "locations" || currentRoute == "map") {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to weather"
                            )
                        }
                    }
                },

                actions = {
                    if (currentRoute != "locations" && currentRoute != "map") {

                        // Open the Locations screen
                        IconButton(
                            onClick = {
                                navController.navigate("locations") {
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = "Manage locations"
                            )
                        }

                        // Celsius / Fahrenheit selector
                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            SegmentedButton(
                                selected = selectedUnit ==
                                        TemperatureUnit.CELSIUS,
                                onClick = {
                                    selectedUnit =
                                        TemperatureUnit.CELSIUS
                                },
                                shape =
                                    SegmentedButtonDefaults.itemShape(
                                        index = 0,
                                        count = 2
                                    ),
                                icon = {},
                                label = {
                                    Text("°C")
                                }
                            )

                            SegmentedButton(
                                selected = selectedUnit ==
                                        TemperatureUnit.FAHRENHEIT,
                                onClick = {
                                    selectedUnit =
                                        TemperatureUnit.FAHRENHEIT
                                },
                                shape =
                                    SegmentedButtonDefaults.itemShape(
                                        index = 1,
                                        count = 2
                                    ),
                                icon = {},
                                label = {
                                    Text("°F")
                                }
                            )
                        }
                    }
                }
            )
        },

        bottomBar = {
            if (currentRoute != "locations" && currentRoute != "map") {
                WeatherBottomNavigation(
                    selectedIndex = selectedIndex,
                    onTabSelected = { index ->
                        val route = if (index == 0) "current" else "daily"

                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "current",
            modifier = Modifier.padding(innerPadding)
        ){
            composable("current") {

                val currentWeather = weatherState.weather?.current

                when {
                    weatherState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    weatherState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = weatherState.errorMessage
                                    ?: "Unable to load weather."
                            )

                            Button(
                                onClick = { weatherViewModel.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }

                    currentWeather != null -> {
                        CurrentWeatherScreen(
                            weather = currentWeather,
                            hourlyForecast = weatherState.weather?.hourly
                                ?: emptyList(),
                            unit = selectedUnit
                        )
                    }

                    else -> {
                        Text("No weather data available.")
                    }
                }
            }

            composable("daily") {

                val weatherSnapshot = weatherState.weather

                when {
                    weatherState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    weatherState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = weatherState.errorMessage
                                    ?: "Unable to load forecast."
                            )

                            Button(
                                onClick = { weatherViewModel.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }

                    weatherSnapshot != null -> {
                        DailyForecastScreen(
                            forecast = weatherSnapshot.daily,
                            location = weatherSnapshot.current.location,
                            unit = selectedUnit
                        )
                    }

                    else -> {
                        Text("No forecast data available.")
                    }
                }
            }

            // Dedicated Mapbox screen
            composable("map") {
                LocationMapScreen(
                    selectedLocation = weatherViewModel.selectedLocation,
                    isLocating = isLocating,
                    locationError = locationError,
                    onUseMyLocation = { requestMyLocation() }
                )
            }

            composable("locations") {
                LocationsScreen(
                    selectedLocation = weatherViewModel.selectedLocation,
                    savedLocations = weatherViewModel.savedLocations,
                    searchState = weatherViewModel.citySearchState,

                    onSearch = { query ->
                        weatherViewModel.searchCanadianCities(query)
                    },

                    onSelect = { location ->
                        weatherViewModel.selectLocation(location)
                        navController.popBackStack()
                    },

                    onSave = { location ->
                        weatherViewModel.saveLocation(location)
                    },

                    onRemove = { location ->
                        weatherViewModel.removeLocation(location)
                    },

                    isLocating = isLocating,

                    locationError = locationError,

                    onMyLocation = {
                        requestMyLocation()
                    },

                    onViewMap = {
                        navController.navigate("map") {
                            launchSingleTop = true
                        }
                    },

                    lastGpsName = weatherViewModel.lastGpsName,
                    lastGpsTimestamp = weatherViewModel.lastGpsTimestamp,
                )
            }
        }
    }
}