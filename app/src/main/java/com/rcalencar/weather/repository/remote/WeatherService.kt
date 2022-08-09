package com.rcalencar.weather.repository.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("rcalencar/ComposeWeather/master/{id}.json")
    suspend fun getWeather(@Path("id") id: Long): WeatherInformation
}