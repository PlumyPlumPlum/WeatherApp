WeatherApp — Phase 2
NSCC MOBI3002 · Assignment 2 — Weather App, Phase 2

WeatherApp is an Android weather application built with Kotlin, Jetpack Compose, and Material 3. It displays live, location-based conditions using WeatherAPI.com, including current weather, a three-day daily forecast, and a scrolling 24-hour outlook. Users can detect their device location, search for Canadian cities, and save locations for future use.
Screenshots
Current Weather	3-Day Forecast	Locations
		
<img width="405" height="901" alt="image" src="https://github.com/user-attachments/assets/ae111600-fcde-4ba2-b9d8-49eebca45d00" />

<img width="410" height="903" alt="image" src="https://github.com/user-attachments/assets/d7e96b1e-c2f7-4641-ab91-0b803ead8ed3" />

These screenshots should be updated to show the final Phase 2 app before submission.
Assignment requirements
#	Requirement	Implementation
1	Live weather API	Retrofit/Gson integration with WeatherAPI.com.
2	Current conditions	Condition icon and description, temperature, feels-like temperature, wind direction/speed, humidity, precipitation chance and current precipitation amount.
3	Daily forecast	Three forecast days, each with a date, condition icon/description, high/low temperatures, maximum wind speed, precipitation probability/amount and humidity.
4	Device location at app startup	Requests location permission as needed and retrieves coordinates to display local weather.
5	Selected location in the top bar	Current Weather and Daily Forecast use the same selected location.
6 (optional)	Locations screen	Opens from the location icon and provides My Location, saved cities and city search.
7 (optional)	GPS city on Locations screen	Displays the last detected city and GPS-fix time; provides a way to refresh location.
8 (optional)	Canadian city search and persistence	Filters search results to Canada, supports saving/selecting/removing cities, prevents duplicates and remembers saved locations across app restarts.


Additional features
- Next 24 Hours: Horizontally scrolling hourly weather outlook.
- Mapbox map: Separate map view for device/selected-location context.
- Celsius/Fahrenheit toggle: Change displayed temperature units without changing the underlying weather data.
- Custom interface: Material 3 cards, weather illustrations, dark/light theme support, launcher icon and splash screen.
- Location fallback: The app can be used with manually selected Canadian cities if device-location permission is unavailable or denied.
Technology
- Kotlin, Jetpack Compose, Material 3 and Navigation Compose
- Retrofit and Gson for WeatherAPI.com requests
- AndroidX ViewModel and Kotlin coroutines for weather state
- Mapbox SDK for device-location/map functionality
- SharedPreferences and Gson for selected/saved locations
- Gradle Kotlin DSL and version catalog
- Minimum SDK: Android 8.0 (API 26)
Run the app
1. Clone the repository:
   git clone https://github.com/PlumyPlumPlum/WeatherApp.git
2. Open the WeatherApp project in Android Studio and allow Gradle to sync.
3. Obtain a WeatherAPI.com API key. In the project-root local.properties file, add:
   WEATHER_API_KEY=your_weatherapi_key_here
   Keep your actual key private. local.properties is excluded by .gitignore.
4. The project uses a public Mapbox access token in app/src/main/res/values/mapbox_access_token.xml. If using your own token, configure a valid Mapbox public token there and restrict its permissions through your Mapbox account.
5. Launch an emulator or Android device running API 26 or newer, with internet access and location services enabled. Select the app run configuration and press Run.
6. Grant location permission when prompted to load local weather. If using an emulator, configure its simulated location in Extended Controls → Location.
Note: Live weather requires internet access and a valid WeatherAPI key. If GPS permission is denied, users can still search for and choose a Canadian city.
Code organization
- WeatherApp.kt — Main Compose scaffold, navigation, unit toggle and device-location interaction.
- ui/screens/CurrentWeatherScreen.kt — Current conditions and hourly outlook.
- ui/screens/DailyForecastScreen.kt — Three-day forecast cards.
- ui/screens/LocationsScreen.kt — Canadian search, GPS information and saved-location management.
- ui/screens/LocationMapScreen.kt — Mapbox location map.
- ui/viewmodel/WeatherViewModel.kt — Weather/loading/search/location UI state.
- data/WeatherRepository.kt — Converts WeatherAPI responses into models displayed by the app.
- data/remote/WeatherApiService.kt and WeatherApiModels.kt — API endpoints and response models.
- data/LocationStore.kt — Persists selected cities, saved cities and most recent GPS details.
- ui/components/WeatherIcon.kt — Maps weather conditions to app illustrations.
Manual verification
The following user flows were checked on an Android emulator during Phase 2:
- Launching the app and granting location permission to retrieve local weather.
- Displaying the GPS-detected city on the Locations screen.
- Showing current weather measurements and a live three-day daily forecast.
- Searching for a Canadian city (including London, Ontario).
- Saving a city and viewing the matching weather on both Now and Daily screens.
- Keeping saved locations after the app is closed and reopened.
- Removing a saved city without affecting other entries.
- Preventing duplicate saved cities.
Phase 1 to Phase 2
Phase 1 established the Compose interface, visual theme, navigation and placeholder weather data. Phase 2 replaced the displayed placeholders with live WeatherAPI data, added GPS-based weather, and implemented the optional Locations workflow with Canadian city search and persistence. The hourly forecast and Mapbox view extend the assignment's baseline functionality.
