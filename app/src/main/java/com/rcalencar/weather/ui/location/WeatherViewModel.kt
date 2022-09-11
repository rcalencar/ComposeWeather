package com.rcalencar.weather.ui.location

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcalencar.weather.dateToString
import com.rcalencar.weather.isSameDay
import com.rcalencar.weather.persist
import com.rcalencar.weather.repository.Status
import com.rcalencar.weather.repository.WeatherRepository
import com.rcalencar.weather.repository.remote.weather.WeatherEntry
import com.rcalencar.weather.repository.remote.weather.WeatherInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

enum class TemperatureUnit {
    C,
    F
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    sharedPreferences: SharedPreferences,
) : ViewModel() {
    private var currentWeatherId: Long? = null

    var state by mutableStateOf(WeatherUiState())
        private set

    var temperatureUnit by sharedPreferences.persist(
        TemperatureUnit.C,
        toString = { it.name },
        fromString = { TemperatureUnit.valueOf(it) }
    )
        private set

    private fun refresh() {
        currentWeatherId?.let {
            fetchWeather(it)
        }
    }

    fun fetchWeather(id: Long) {
        currentWeatherId = id
        state = state.copy(
            loading = true,
            currentWeather = emptyCurrentWeather,
            errorMessage = null
        )
        viewModelScope.launch {
            val result = repository.loadWeatherInformation(id)
            when (result.status) {
                Status.SUCCESS -> {
                    state = state.copy(
                        loading = false,
                        currentWeather = result.data?.toCurrentWeather(temperatureUnit)
                            ?: emptyCurrentWeather,
                        errorMessage = null
                    )
                }
                Status.ERROR -> {
                    state = state.copy(
                        loading = false,
                        currentWeather = emptyCurrentWeather,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun toggleUnit() {
        temperatureUnit = if (temperatureUnit == TemperatureUnit.F) {
            TemperatureUnit.C
        } else {
            TemperatureUnit.F
        }
        refresh()
    }
}

fun WeatherInformation.toCurrentWeather(unit: TemperatureUnit): CurrentWeather {
    val currentEntry =
        this.consolidatedWeather.firstOrNull { this.time.isSameDay(it.applicableDate) }
    val forecast =
        this.consolidatedWeather.filter { it.applicableDate > currentEntry?.applicableDate }
            .sortedBy { it.applicableDate }.map { it.toForecastWeather(unit) }
    return currentEntry?.let {
        CurrentWeather(
            title = this.title,
            weatherStateName = currentEntry.weatherStateName,
            weatherStateAbbr = currentEntry.weatherStateAbbr,
            minTemp = convert(currentEntry.minTemp, unit).roundToInt().toString(),
            maxTemp = convert(currentEntry.maxTemp, unit).roundToInt().toString(),
            theTemp = convert(currentEntry.theTemp, unit).roundToInt().toString(),
            forecast = forecast
        )
    } ?: emptyCurrentWeather
}

fun WeatherEntry.toForecastWeather(unit: TemperatureUnit): ForecastWeather {
    return ForecastWeather(
        applicableDate = this.applicableDate.dateToString("MMM dd"),
        weatherStateName = this.weatherStateName,
        weatherStateAbbr = this.weatherStateAbbr,
        minTemp = convert(this.minTemp, unit).roundToInt().toString(),
        maxTemp = convert(this.maxTemp, unit).roundToInt().toString(),
    )
}

private fun convert(value: Float, unit: TemperatureUnit): Float {
    return if (unit == TemperatureUnit.C) value
    else cToF(value)
}

private fun cToF(temp: Float): Float {
    return (temp * 1.8f) + 32
}