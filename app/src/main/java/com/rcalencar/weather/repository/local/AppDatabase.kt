package com.rcalencar.weather.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rcalencar.weather.repository.remote.locations.Location
import com.rcalencar.weather.repository.remote.weather.WeatherInformation

@Database(entities = [WeatherInformation::class, Location::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WeatherInformationDao(): WeatherInformationDao

    abstract fun LocationDao(): LocationDao
}