package com.rcalencar.weather

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.rcalencar.weather.repository.remote.WeatherService
import com.rcalencar.weather.repository.remote.weatherService
import com.rcalencar.weather.repository.local.AppDatabase
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

    @Singleton
    @Provides
    fun provideAppSharedPreference(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            appContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }
}
