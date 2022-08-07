package com.rcalencar.weather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun weatherService() : WeatherService {
    return Retrofit.Builder()
        .baseUrl("https://host/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherService::class.java)
}