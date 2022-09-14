package com.rcalencar.weather.ui.location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rcalencar.weather.R
import com.rcalencar.weather.repository.remote.locations.Location
import com.rcalencar.weather.ui.theme.WeatherTheme
import com.rcalencar.weather.ui.weather.WeatherUiState
import com.rcalencar.weather.ui.weather.WeatherViewModel

@Composable
fun LocationsScreen(
    locationViewModel: LocationViewModel = hiltViewModel(),
    onLocationClick: (Long) -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        locationViewModel.fetchLocations()
    }
    val locationsUiState = locationViewModel.state
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LocationListComposable(locationsUiState, onLocationClick)
    }
}

@Composable
private fun LocationListComposable(
    locationsUiState: LocationsUiState,
    onLocationClick: (Long) -> Unit = { }
) {
    if (locationsUiState.loading) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        }
    } else {
        LazyColumn(
            state = rememberLazyListState()
        ) {
            items(locationsUiState.locations) { item ->
                LocationRowItem(item, onLocationClick)
            }
        }
    }
}

@Composable
private fun LocationRowItem(
    item: Location,
    onLocationClick: (Long) -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        weatherViewModel.fetchWeather(item.woeid)
    }
    val weatherUiState = weatherViewModel.state
    LocationRowItemComposable(weatherUiState, item, onLocationClick)
}

@Composable
private fun LocationRowItemComposable(
    weatherUiState: WeatherUiState,
    item: Location,
    onLocationClick: (Long) -> Unit = {}
) {
    Row {
        Button(onClick = { onLocationClick(item.woeid) }) {
            Text(text = item.title)
        }
        AsyncImage(
            model = stringResource(
                R.string.image_url,
                weatherUiState.currentWeather.weatherStateAbbr
            ),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .width(55.dp),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherTheme {
        LocationRowItemComposable(
            weatherUiState = WeatherUiState(),
            item = Location(1L, "Montreal", "", "")
        )
    }
}