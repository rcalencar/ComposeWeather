package com.rcalencar.weather.ui.location

data class CurrentWeather(
    val title: String,
    val weatherStateName: String,
    val weatherStateAbbr: String,
    val minTemp: String,
    val maxTemp: String,
    val theTemp: String,
    val forecast: List<ForecastWeather>,
)

data class ForecastWeather(
    val applicableDate: String,
    val weatherStateName: String,
    val weatherStateAbbr: String,
    val minTemp: String,
    val maxTemp: String,
)

val emptyCurrentWeather = CurrentWeather(
    title = "-",
    weatherStateName = "-",
    weatherStateAbbr = "-",
    minTemp = "-",
    maxTemp = "-",
    theTemp = "-",
    forecast = listOf(),
)