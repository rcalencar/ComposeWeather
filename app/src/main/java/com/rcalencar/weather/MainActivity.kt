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
import com.rcalencar.weather.ui.location.LocationScreen
import com.rcalencar.weather.ui.location.WeatherScreen
import com.rcalencar.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }

    @Composable
    private fun WeatherApp() {
        WeatherTheme {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Home.route,
            ) {
                composable(route = Home.route) {
                    WeatherScreen(
                        onLocationClick = { locationId ->
                            navController.navigate("${Location.route}/$locationId")
                        })
                }
                composable(
                    route = Location.routeWithArgs,
                    arguments = Location.arguments
                ) { navBackStackEntry ->
                    val locationId =
                        navBackStackEntry.arguments?.getLong(Location.locationIdArg) ?: 0
                    LocationScreen(locationId)
                }
            }
        }
    }
}

interface Destination {
    val route: String
    val screen: @Composable () -> Unit
}

object Home : Destination {
    override val route = "home"
    override val screen: @Composable () -> Unit = { WeatherScreen() }
}

object Location : Destination {
    override val route = "location"
    override val screen: @Composable () -> Unit = { LocationScreen() }
    const val locationIdArg = "location_id"
    val routeWithArgs = "$route/{$locationIdArg}"
    val arguments = listOf(
        navArgument(locationIdArg) { type = NavType.LongType }
    )
}