package com.rcalencar.weather.repository

import com.rcalencar.weather.api.WeatherInformationApiModel
import com.rcalencar.weather.api.WeatherService
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {
    var data: WeatherInformationApiModel? = null

    suspend fun loadWeatherInformation(id: Long) : Resource<WeatherInformationApiModel> {
        return if (data == null) {
            delay(1900L)
            return try {
                data = weatherService.getWeather(id)
                Resource.success(data = data)
            } catch (exception: Exception) {
                Resource.error(data = null, message = exception.message ?: "Error Occurred!")
            }
        } else {
            Resource.success(data = data)
        }
    }
}

