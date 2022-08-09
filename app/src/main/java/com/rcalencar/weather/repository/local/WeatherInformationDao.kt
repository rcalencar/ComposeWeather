package com.rcalencar.weather.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rcalencar.weather.repository.remote.WeatherInformation

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