package com.example.weatherapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults


@Composable
fun WeatherBottomNavigation(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    NavigationBar {

        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { onTabSelected(0) },
            colors = itemColors,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null
                )
            },
            label = {
                Text("Now")
            }
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { onTabSelected(1) },
            colors = itemColors,
            icon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null
                )
            },
            label = {
                Text("Daily")
            }
        )
    }
}