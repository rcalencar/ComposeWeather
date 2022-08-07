package com.rcalencar.weather.api

import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("mobile-take-home/{id}.json")
    suspend fun getWeather(@Path("id") id: Long): WeatherInformationApiModel
}