package com.rcalencar.weather.ui.location

data class LocationUiState(
    val loading: Boolean = false,
    val currentWeather: CurrentWeather = emptyCurrentWeather,
    val errorMessage: String? = null,
)