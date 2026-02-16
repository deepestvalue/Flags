package org.k.flags

import WeatherRoute
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.k.flags.generated.resources.Res
import org.k.flags.generated.resources.arrow_back_2_24px
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.setValue
import org.k.flags.generated.resources.close_24px
import org.k.flags.generated.resources.search_24px


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(countriesCachePath: String) {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        var searchQuery by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Flags") },
                    navigationIcon = {
                        if (currentRoute?.hasRoute<WeatherRoute>() == true) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    // Use painterResource for the local XML file
                                    painter = painterResource(Res.drawable.arrow_back_2_24px),
                                    contentDescription = "Go Back",

                                    // You can change size or color easily
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search countries...") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.search_24px),
                                        contentDescription = "Search"
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(
                                                painter = painterResource(Res.drawable.close_24px),
                                                contentDescription = "Clear Search"
                                            )
                                        }
                                    }
                                },
                                singleLine = true
                            )
                        }
                    }
                )
            }
        ) {innerPadding ->
            Box( modifier = Modifier.padding(innerPadding)) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(countriesCachePath, searchQuery) { cityName, lat, lng ->
                            // Use the data class for navigation
                            navController.navigate(WeatherRoute(cityName, lat.toFloat(), lng.toFloat()))
                        }
                    }
                    // Use the class type here instead of a string path
                    composable<WeatherRoute> { backStackEntry ->
                        // Use .toRoute() to automatically extract arguments!
                        val args: WeatherRoute = backStackEntry.toRoute()

                        WeatherScreen(
                            cityName = args.cityName,
                            lat = args.lat.toDouble(),
                            long = args.long.toDouble()
                        )
                    }
                }
            }
        }
    }
}