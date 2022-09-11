package com.rcalencar.weather.ui.location

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rcalencar.weather.repository.Status
import com.rcalencar.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    var state by mutableStateOf(LocationsUiState())
        private set

    fun fetchLocations() {
        state = state.copy(
            loading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = repository.loadLocations()
            when(result.status) {
                Status.SUCCESS -> {
                    state = state.copy(
                        loading = false,
                        locations = result.data ?: emptyList(),
                        errorMessage = null
                    )
                }
                Status.ERROR -> {
                    state = state.copy(
                        loading = false,
                        locations = emptyList(),
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}