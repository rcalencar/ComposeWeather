package com.rcalencar.weather.repository.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class WeatherEntry(
    @field:SerializedName("id")
    val id: Long,
    @field:SerializedName("weather_state_name")
    val weatherStateName: String,
    @field:SerializedName("weather_state_abbr")
    val weatherStateAbbr: String,
    @field:SerializedName("applicable_date")
    val applicableDate: Date,
    @field:SerializedName("min_temp")
    val minTemp: Float,
    @field:SerializedName("max_temp")
    val maxTemp: Float,
    @field:SerializedName("the_temp")
    val theTemp: Float,
): Serializable