package com.rcalencar.weather.repository.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun weatherService() : WeatherService {
    return Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherService::class.java)
}