package com.rcalencar.weather.ui

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rcalencar.weather.repository.Resource
import com.rcalencar.weather.repository.WeatherRepository
import com.rcalencar.weather.ui.weather.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class WeatherUnitTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = Mockito.mock(WeatherRepository::class.java)
    private val sharedPref = Mockito.mock(SharedPreferences::class.java)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFromViewModelToRepositoryCall() = runTest {
        `when`(repository.loadWeatherInformation(1L)).thenReturn(Resource.success())
        val mainViewModel = WeatherViewModel(repository, sharedPref)
        mainViewModel.fetchWeather(1L)
        advanceUntilIdle()
        verify(repository).loadWeatherInformation(1L)
    }
}