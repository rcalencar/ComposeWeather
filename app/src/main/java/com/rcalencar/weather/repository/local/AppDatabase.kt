package com.rcalencar.weather.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rcalencar.weather.repository.remote.WeatherInformation

@Database(entities = [WeatherInformation::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WeatherInformationDao(): WeatherInformationDao
}