package org.k.flags

import androidx.compose.runtime.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.k.flags.network.WeatherAPI
import org.k.flags.weather.Weather
import org.k.flags.weather.celsiusToFahrenheit
import kotlin.math.roundToInt

@Composable
fun WeatherCard(modifier: Modifier, cityName: String, lat: Double, long: Double) {
    var weather: Weather? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        weather = WeatherAPI().getWeather(lat, long)
    }


    weather?.let { weather ->
        Card(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = cityName,
                    style = MaterialTheme.typography.headlineMedium
                )
                AsyncImage(
                    modifier = Modifier.width(128.dp).height(128.dp).align(CenterHorizontally),
                    model = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png",
                    contentDescription = weather.weather[0].description,
                    contentScale = ContentScale.Fit
                )
                Text(
                    "Feels like: ${weather.main.feels_like.roundToInt()} 'C / ${celsiusToFahrenheit(weather.main.feels_like)} 'F",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Temp: ${weather.main.temp.roundToInt()} 'C / ${celsiusToFahrenheit(weather.main.temp)} 'F",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}