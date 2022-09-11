package com.rcalencar.weather.ui.location

data class WeatherUiState(
    val loading: Boolean = false,
    val currentWeather: CurrentWeather = emptyCurrentWeather,
    val errorMessage: String? = null,
)