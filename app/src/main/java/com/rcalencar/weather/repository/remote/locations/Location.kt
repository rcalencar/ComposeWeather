package com.rcalencar.weather.repository.remote.locations

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Location(
    @PrimaryKey
    @field:SerializedName("woeid")
    val woeid: Long,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("location_type")
    val locationType: String,

    @field:SerializedName("timezone")
    val timezone: String
) : Serializable