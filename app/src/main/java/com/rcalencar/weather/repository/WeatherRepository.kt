package com.rcalencar.weather.repository

import com.rcalencar.weather.AppDatabase
import com.rcalencar.weather.api.WeatherInformation
import com.rcalencar.weather.api.WeatherService
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService,
    private val appDatabase: AppDatabase
) {
    suspend fun loadWeatherInformation(id: Long): Resource<WeatherInformation> {
        val dao = appDatabase.WeatherInformationDao()
        val weatherInformation = dao.loadById(id)
        return if (weatherInformation == null) {
            delay(1900L)
            return try {
                val data = weatherService.getWeather(id)
                dao.insertAll(data)
                Resource.success(data = data)
            } catch (exception: Exception) {
                Resource.error(data = null, message = exception.message ?: "Error Occurred!")
            }
        } else {
            Resource.success(data = weatherInformation)
        }
    }
}

