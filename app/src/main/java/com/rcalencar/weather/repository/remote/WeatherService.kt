package com.rcalencar.weather.repository.remote

import com.rcalencar.weather.repository.remote.locations.Location
import com.rcalencar.weather.repository.remote.weather.WeatherInformation
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("rcalencar/ComposeWeather/master/{id}.json")
    suspend fun getWeather(@Path("id") id: Long): WeatherInformation

    @GET("rcalencar/ComposeWeather/master/locations.json")
    suspend fun getLocations(): List<Location>
}