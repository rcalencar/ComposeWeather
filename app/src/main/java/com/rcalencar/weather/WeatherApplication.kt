package com.rcalencar.weather

import android.app.Application
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rcalencar.weather.api.WeatherEntry
import com.rcalencar.weather.api.WeatherInformation
import dagger.hilt.android.HiltAndroidApp
import java.lang.reflect.Type
import java.util.*


@HiltAndroidApp
class WeatherApplication : Application()

@Dao
interface WeatherInformationDao {
    @Query("SELECT * FROM WeatherInformation")
    suspend fun getAll(): List<WeatherInformation>

    @Query("SELECT * FROM WeatherInformation WHERE woeid = :woeid")
    suspend fun loadById(woeid: Long): WeatherInformation?

    @Insert
    suspend fun insertAll(vararg weatherInformation: WeatherInformation)

    @Update
    suspend fun update(weatherInformation: WeatherInformation)
}

@Database(entities = [WeatherInformation::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WeatherInformationDao(): WeatherInformationDao
}

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
