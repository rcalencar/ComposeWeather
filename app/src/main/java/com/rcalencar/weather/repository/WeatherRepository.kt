package com.rcalencar.weather.repository

import com.rcalencar.weather.repository.local.AppDatabase
import com.rcalencar.weather.repository.remote.WeatherInformation
import com.rcalencar.weather.repository.remote.WeatherService
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
            delay(3000L)
            return try {
                val data = weatherService.getWeather(id)
                dao.insertAll(data)
                Resource.success(data = data)
            } catch (exception: Exception) {
                Resource.error(data = null, message = exception.message ?: "Error Occurred!")
            }
        } else {
            delay(1000L)
            Resource.success(data = weatherInformation)
        }
    }
}

