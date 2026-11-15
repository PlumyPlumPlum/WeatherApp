
package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.SavedLocation
import com.example.weatherapp.ui.viewmodel.CitySearchUiState
import java.text.DateFormat
import java.util.Date
import java.util.Locale
@Composable
fun LocationsScreen(
    selectedLocation: SavedLocation,
    savedLocations: List<SavedLocation>,
    searchState: CitySearchUiState,
    onSearch: (String) -> Unit,
    onSelect: (SavedLocation) -> Unit,
    onSave: (SavedLocation) -> Unit,
    onRemove: (SavedLocation) -> Unit,
    isLocating: Boolean,
    locationError: String?,
    lastGpsName: String?,
    lastGpsTimestamp: Long,
    onMyLocation: () -> Unit,
    onViewMap: () -> Unit
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Find a Canadian city",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("City name") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { onSearch(searchText) },
                enabled = searchText.trim().length >= 2 &&
                        !searchState.isLoading
            ) {
                Text("Search")
            }
        }

// My Location section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "My Location",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Use your device's location to view local weather.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Display the last location identified through GPS.
                if (lastGpsName != null) {
                    Text(
                        text = "Last detected: $lastGpsName",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Show when the GPS coordinates were obtained.
                if (lastGpsTimestamp > 0L) {
                    val formattedTime = DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM,
                        DateFormat.SHORT,
                        Locale.CANADA
                    ).format(Date(lastGpsTimestamp))

                    Text(
                        text = if (lastGpsName != null) {
                            "GPS fix: $formattedTime · Tap again to refresh."
                        } else {
                            "GPS fix obtained at $formattedTime. " +
                                    "City identification is pending or unavailable."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onMyLocation,
                    enabled = !isLocating
                ) {
                    if (isLocating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )

                        Spacer(Modifier.width(8.dp))
                    }

                    Text(
                        text = if (isLocating) {
                            "Finding location..."
                        } else {
                            "Use My Location"
                        }
                    )
                }

                // Open the map after GPS retrieval; also works for a selected city.
                OutlinedButton(onClick = onViewMap) {
                    Text("View Map")
                }

                if (locationError != null) {
                    Text(
                        text = locationError,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Saved Locations",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (savedLocations.isEmpty()) {
                item {
                    Text(
                        text = "No saved cities yet.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(
                savedLocations,
                key = { "saved_${it.query}" }
            ) { location ->
                LocationCard(
                    location = location,
                    isSelected = location.query ==
                            selectedLocation.query,
                    isSaved = true,
                    onSelect = { onSelect(location) },
                    onSave = { onSave(location) },
                    onRemove = { onRemove(location) }
                )
            }

            item {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (searchState.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (searchState.errorMessage != null) {
                item {
                    Text(
                        text = searchState.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (
                searchState.hasSearched &&
                !searchState.isLoading &&
                searchState.locations.isEmpty() &&
                searchState.errorMessage == null
            ) {
                item {
                    Text("No matching Canadian cities found.")
                }
            }

            items(
                searchState.locations,
                key = { "result_${it.query}" }
            ) { location ->

                val isSaved = savedLocations.any {
                    it.query == location.query
                }

                LocationCard(
                    location = location,
                    isSelected = location.query ==
                            selectedLocation.query,
                    isSaved = isSaved,
                    onSelect = { onSelect(location) },
                    onSave = { onSave(location) },
                    onRemove = { onRemove(location) }
                )
            }
        }
    }
}

@Composable
private fun LocationCard(
    location: SavedLocation,
    isSelected: Boolean,
    isSaved: Boolean,
    onSelect: () -> Unit,
    onSave: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = location.displayName,
                style = MaterialTheme.typography.titleMedium
            )

            if (isSelected) {
                Text(
                    text = "Currently selected",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onSelect) {
                    Text("View")
                }

                if (isSaved) {
                    TextButton(onClick = onRemove) {
                        Text("Remove")
                    }
                } else {
                    Button(onClick = onSave) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
