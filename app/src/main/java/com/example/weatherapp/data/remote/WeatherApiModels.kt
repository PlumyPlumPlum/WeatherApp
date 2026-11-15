package com.example.weatherapp.data.remote

import com.google.gson.annotations.SerializedName

data class ApiWeatherResponse(
    val location: ApiLocation,
    val current: ApiCurrent,
    val forecast: ApiForecast
)

data class ApiLocation(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,

    @SerializedName("tz_id")
    val timeZoneId: String? = null
)

data class ApiCondition(
    val text: String,
    val code: Int
)

data class ApiCurrent(
    @SerializedName("temp_c")
    val temperatureC: Double,

    @SerializedName("feelslike_c")
    val feelsLikeC: Double,

    @SerializedName("wind_kph")
    val windKph: Double,

    @SerializedName("wind_dir")
    val windDirection: String,

    @SerializedName("precip_mm")
    val precipitationMm: Double,

    val humidity: Int,
    val condition: ApiCondition
)

data class ApiForecast(
    val forecastday: List<ApiForecastDay>
)

data class ApiForecastDay(
    val date: String,
    val day: ApiDay,
    val hour: List<ApiHour>
)

data class ApiDay(
    @SerializedName("maxtemp_c")
    val highC: Double,

    @SerializedName("mintemp_c")
    val lowC: Double,

    @SerializedName("maxwind_kph")
    val windKph: Double,

    @SerializedName("avghumidity")
    val humidity: Int,

    @SerializedName("totalprecip_mm")
    val precipitationMm: Double,

    @SerializedName("daily_chance_of_rain")
    val chanceOfRain: Int,

    @SerializedName("daily_chance_of_snow")
    val chanceOfSnow: Int,

    val condition: ApiCondition
)

data class ApiHour(
    @SerializedName("time_epoch")
    val timeEpoch: Long,

    @SerializedName("temp_c")
    val temperatureC: Double,

    @SerializedName("precip_mm")
    val precipitationMm: Double,

    @SerializedName("chance_of_rain")
    val chanceOfRain: Int,

    @SerializedName("chance_of_snow")
    val chanceOfSnow: Int,

    val condition: ApiCondition
)
