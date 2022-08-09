package com.rcalencar.weather.repository.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rcalencar.weather.repository.remote.WeatherEntry
import java.lang.reflect.Type
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): List<WeatherEntry> {
        val listType: Type = object : TypeToken<List<WeatherEntry?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<WeatherEntry?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}