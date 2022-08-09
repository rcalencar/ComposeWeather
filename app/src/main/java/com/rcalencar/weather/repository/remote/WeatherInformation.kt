package com.rcalencar.weather.repository.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity
data class WeatherInformation(
    @PrimaryKey
    @field:SerializedName("woeid")
    val woeid: Long,
    @field:SerializedName("consolidated_weather")
    val consolidatedWeather: List<WeatherEntry>,
    @field:SerializedName("time")
    val time: Date,
    @field:SerializedName("title")
    val title: String,
): Serializable