package com.example.weatherapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.SampleWeatherData
import com.example.weatherapp.ui.components.WeatherBottomNavigation
import com.example.weatherapp.ui.screens.CurrentWeatherScreen
import com.example.weatherapp.ui.screens.DailyForecastScreen
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.weatherapp.ui.TemperatureUnit

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                title = {
                    Text("Weather")
                },
                actions = {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        SegmentedButton(
                            selected = selectedUnit == TemperatureUnit.CELSIUS,
                            onClick = {
                                selectedUnit = TemperatureUnit.CELSIUS
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = 0,
                                count = 2
                            ),
                            icon = {},
                            label = {
                                Text("°C")
                            }
                        )

                        SegmentedButton(
                            selected = selectedUnit == TemperatureUnit.FAHRENHEIT,
                            onClick = {
                                selectedUnit = TemperatureUnit.FAHRENHEIT
                            },
                            shape = SegmentedButtonDefaults.itemShape(
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
            )
        },


        bottomBar = {
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
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "current",
            modifier = Modifier.padding(innerPadding)
        ){

            composable("current") {
                CurrentWeatherScreen(
                    weather = SampleWeatherData.currentWeather,
                    unit = selectedUnit
                )
            }

            composable("daily") {
                DailyForecastScreen(
                    forecast = SampleWeatherData.dailyForecast,
                    unit = selectedUnit
                )
            }
        }
    }
}