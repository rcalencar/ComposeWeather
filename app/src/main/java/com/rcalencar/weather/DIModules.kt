package com.rcalencar.weather

import com.rcalencar.weather.api.WeatherService
import com.rcalencar.weather.api.weatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ServiceModules {

    @Singleton
    @Provides
    fun provideWeatherService(): WeatherService {
        return weatherService()
    }
}
