package com.rcalencar.weather.ui.location

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rcalencar.weather.R
import com.rcalencar.weather.repository.remote.locations.Location
import com.rcalencar.weather.ui.theme.WeatherTheme

@Composable
fun LocationsScreen(
    locationViewModel: LocationViewModel = hiltViewModel(),
    onLocationClick: (Long) -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        locationViewModel.fetchLocations()
    }
    val locationsUiState = locationViewModel.state
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


@Composable
fun WeatherScreen(
    id: Long = 0,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        weatherViewModel.fetchWeather(id)
    }
    val weatherUiState = weatherViewModel.state
    val unit = weatherViewModel.temperatureUnit
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        WeatherInfoComposable(weatherUiState, unit.name, onUnitClick = { weatherViewModel.toggleUnit() })
    }
}

@Composable
private fun WeatherInfoComposable(
    weatherUiState: WeatherUiState,
    unit: String,
    onUnitClick: () -> Unit = {}
) {
    if (weatherUiState.loading) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        }
    } else {
        Column(modifier = Modifier.padding(top = 56.dp)) {
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(modifier = Modifier
                    .clickable { onUnitClick() }
                    .padding(8.dp),
                    text = unit)
            }
            CurrentWeather(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                weatherUiState = weatherUiState
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                state = rememberLazyListState()
            ) {
                itemsIndexed(weatherUiState.currentWeather.forecast) { index, item ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        ForecastWeather(
                            modifier = Modifier.padding(12.dp),
                            forecastWeather = item,
                            index = index
                        )
                    }
                    if (index == weatherUiState.currentWeather.forecast.lastIndex) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentWeather(
    modifier: Modifier = Modifier,
    weatherUiState: WeatherUiState
) {
    Column(modifier = modifier) {
        Text(
            text = weatherUiState.currentWeather.title,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
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
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        R.string.temperature,
                        weatherUiState.currentWeather.theTemp
                    ),
                    fontSize = 70.sp,
                )
            }
            Text(
                text = weatherUiState.currentWeather.weatherStateName,
                fontSize = 17.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.l_h,
                    weatherUiState.currentWeather.minTemp,
                    weatherUiState.currentWeather.maxTemp
                ),
                fontSize = 26.sp,
            )
            if (!weatherUiState.errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = weatherUiState.errorMessage,
                )
            }
        }
    }
}

@Composable
private fun ForecastWeather(
    modifier: Modifier = Modifier,
    forecastWeather: ForecastWeather,
    index: Int
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (index == 0) stringResource(
                R.string.tomorrow,
                forecastWeather.minTemp,
                forecastWeather.maxTemp
            ) else forecastWeather.applicableDate,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        AsyncImage(
            model = stringResource(
                R.string.image_url,
                forecastWeather.weatherStateAbbr
            ),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .width(34.dp),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(
                R.string.l_h,
                forecastWeather.minTemp,
                forecastWeather.maxTemp
            ),
            fontSize = 14.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val state = WeatherUiState(
        loading = false,
        currentWeather = CurrentWeather(
            title = "Toronto",
            weatherStateName = "Showers",
            weatherStateAbbr = "s",
            minTemp = "6",
            maxTemp = "16",
            theTemp = "12",
            forecast = listOf()
        ),
        errorMessage = "Error"
    )
    WeatherTheme {
        WeatherInfoComposable(state, "C")
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherTheme {
        LocationsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastPreview() {
    val state = ForecastWeather(
        applicableDate = "18 July",
        weatherStateName = "Showers",
        weatherStateAbbr = "s",
        minTemp = "6",
        maxTemp = "16",
    )
    WeatherTheme {
        ForecastWeather(forecastWeather = state, index = 0)
    }
}