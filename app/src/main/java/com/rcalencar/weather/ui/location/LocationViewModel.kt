package com.rcalencar.weather.ui.location

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcalencar.weather.api.WeatherEntryApiModel
import com.rcalencar.weather.api.WeatherInformationApiModel
import com.rcalencar.weather.isSameDay
import com.rcalencar.weather.repository.Status
import com.rcalencar.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    var state by mutableStateOf(LocationUiState())
        private set

    fun fetchWeather(id: Long) {
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
                        currentWeather = result.data?.toCurrentWeather() ?: emptyCurrentWeather,
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
}

fun WeatherInformationApiModel.toCurrentWeather(): CurrentWeather {
    val currentEntry =
        this.consolidatedWeather.firstOrNull { this.time.isSameDay(it.applicableDate) }
    val forecast =
        this.consolidatedWeather.filter { it.applicableDate > currentEntry?.applicableDate }
            .sortedBy { it.applicableDate }.map { it.toForecastWeather() }
    return currentEntry?.let {
        CurrentWeather(
            title = this.title,
            weatherStateName = currentEntry.weatherStateName,
            weatherStateAbbr = currentEntry.weatherStateAbbr,
            minTemp = currentEntry.minTemp.roundToInt().toString(),
            maxTemp = currentEntry.maxTemp.roundToInt().toString(),
            theTemp = currentEntry.theTemp.roundToInt().toString(),
            forecast = forecast
        )
    } ?: emptyCurrentWeather
}

fun WeatherEntryApiModel.toForecastWeather(): ForecastWeather {
    return ForecastWeather(
        applicableDate = this.applicableDate.dateToString("MMM dd"),
        weatherStateName = this.weatherStateName,
        weatherStateAbbr = this.weatherStateAbbr,
        minTemp = this.minTemp.roundToInt().toString(),
        maxTemp = this.maxTemp.roundToInt().toString(),
    )
}

private fun Date.dateToString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}