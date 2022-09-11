package com.rcalencar.weather.ui.location

import com.rcalencar.weather.repository.remote.locations.Location

data class LocationsUiState(
    val loading: Boolean = false,
    val locations: List<Location> = emptyList(),
    val errorMessage: String? = null,
)