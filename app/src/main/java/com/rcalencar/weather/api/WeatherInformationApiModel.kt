package com.rcalencar.weather.api

import com.google.gson.annotations.SerializedName
import java.util.*

data class WeatherInformationApiModel(
    @field:SerializedName("woeid")
    val woeid: Long,
    @field:SerializedName("consolidated_weather")
    val consolidatedWeather: List<WeatherEntryApiModel>,
    @field:SerializedName("time")
    val time: Date,
    @field:SerializedName("title")
    val title: String,
)