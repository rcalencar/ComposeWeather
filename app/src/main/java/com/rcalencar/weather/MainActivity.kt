package com.rcalencar.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rcalencar.weather.ui.weather.WeatherScreen
import com.rcalencar.weather.ui.location.LocationsScreen
import com.rcalencar.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@Composable
private fun WeatherApp() {
    WeatherTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = LocationsDestination.route,
        ) {
            composable(route = LocationsDestination.route) {
                LocationsScreen(
                    onLocationClick = { locationId ->
                        navController.navigate("${WeatherDestination.route}/$locationId")
                    })
            }
            composable(
                route = WeatherDestination.routeWithArgs,
                arguments = WeatherDestination.arguments
            ) { navBackStackEntry ->
                val locationId =
                    navBackStackEntry.arguments?.getLong(WeatherDestination.locationIdArg) ?: 0
                WeatherScreen(locationId)
            }
        }
    }
}

interface Destination {
    val route: String
    val screen: @Composable () -> Unit
}

object LocationsDestination : Destination {
    override val route = "home"
    override val screen: @Composable () -> Unit = { LocationsScreen() }
}

object WeatherDestination : Destination {
    override val route = "location"
    override val screen: @Composable () -> Unit = { WeatherScreen() }
    const val locationIdArg = "location_id"
    val routeWithArgs = "$route/{$locationIdArg}"
    val arguments = listOf(
        navArgument(locationIdArg) { type = NavType.LongType }
    )
}