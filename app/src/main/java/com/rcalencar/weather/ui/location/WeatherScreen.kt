package com.rcalencar.weather.ui.location

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
import androidx.compose.foundation.lazy.LazyRow
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
import com.rcalencar.weather.ui.theme.WeatherTheme

@Composable
fun WeatherScreen(
    onLocationClick: (Long) -> Unit = {}
) {
    Column {
        Button(onClick = { onLocationClick(4418) }) {
            Text(text = "Toronto")
        }
        Button(onClick = { onLocationClick(4419) }) {
            Text(text = "Montreal")
        }
    }
}

@Composable
fun LocationScreen(
    id: Long = 0,
    locationViewModel: LocationViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        locationViewModel.fetchWeather(id)
    }
    val locationUiState = locationViewModel.state
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LocationComposable(locationUiState)
    }
}

@Composable
private fun LocationComposable(locationUiState: LocationUiState) {
    if (locationUiState.loading) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        }
    } else {
        Column(modifier = Modifier.padding(top = 56.dp)) {
            CurrentWeather(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                locationUiState = locationUiState
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                state = rememberLazyListState()
            ) {
                itemsIndexed(locationUiState.currentWeather.forecast) { index, item ->
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
                    if (index == locationUiState.currentWeather.forecast.lastIndex) {
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
    locationUiState: LocationUiState
) {
    Column(modifier = modifier) {
        Text(
            text = locationUiState.currentWeather.title,
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
                        locationUiState.currentWeather.weatherStateAbbr
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
                        locationUiState.currentWeather.theTemp
                    ),
                    fontSize = 70.sp,
                )
            }
            Text(
                text = locationUiState.currentWeather.weatherStateName,
                fontSize = 17.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.l_h,
                    locationUiState.currentWeather.minTemp,
                    locationUiState.currentWeather.maxTemp
                ),
                fontSize = 26.sp,
            )
            if (!locationUiState.errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = locationUiState.errorMessage,
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
    val state = LocationUiState(
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
        LocationComposable(state)
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherTheme {
        WeatherScreen()
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