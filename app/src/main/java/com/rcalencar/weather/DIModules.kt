package com.rcalencar.weather

import android.content.Context
import androidx.room.Room
import com.rcalencar.weather.api.WeatherService
import com.rcalencar.weather.api.weatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java
        ).build()
    }
}
