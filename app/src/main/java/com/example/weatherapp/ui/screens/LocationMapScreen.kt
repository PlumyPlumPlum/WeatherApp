package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.SavedLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.Marker
import java.util.Locale

@OptIn(MapboxExperimental::class)
@Composable
fun LocationMapScreen(
    selectedLocation: SavedLocation,
    isLocating: Boolean,
    locationError: String?,
    onUseMyLocation: () -> Unit
) {
    // Mapbox uses LONGITUDE first, then LATITUDE.
    val point = remember(selectedLocation.latitude, selectedLocation.longitude) {
        Point.fromLngLat(selectedLocation.longitude, selectedLocation.latitude)
    }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(point)
            zoom(14.0)
        }
    }

    // Additional Mapbox feature: smoothly animate the camera when GPS changes.
    LaunchedEffect(point) {
        mapViewportState.easeTo(
            CameraOptions.Builder()
                .center(point)
                .zoom(14.0)
                .build()
        )
    }

    val locationDescription = if (selectedLocation.name == "My Location") {
        "GPS device location"
    } else {
        "Selected city: ${selectedLocation.displayName}"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState
        ) {
            Marker(
                point = point,
                text = if (selectedLocation.name == "My Location") {
                    "My Location"
                } else {
                    selectedLocation.name
                },
                color = Color(0xFF0078D4),
                innerColor = Color.White
            )
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(locationDescription, style = MaterialTheme.typography.titleMedium)
                Text(
                    String.format(
                        Locale.CANADA,
                        "Latitude: %.5f  |  Longitude: %.5f",
                        selectedLocation.latitude,
                        selectedLocation.longitude
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (locationError != null) {
                Card {
                    Text(
                        locationError,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            // Refresh the actual device position, including the emulator's set location.
            Button(onClick = onUseMyLocation, enabled = !isLocating) {
                if (isLocating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text(if (isLocating) "Locating..." else "Use My Location")
            }
            OutlinedButton(
                onClick = {
                    mapViewportState.easeTo(
                        CameraOptions.Builder().center(point).zoom(14.0).build()
                    )
                }
            ) {
                Text("Recenter Map")
            }
        }
    }
}
