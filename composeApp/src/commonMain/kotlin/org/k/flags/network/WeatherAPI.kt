package org.k.flags.network

import org.k.flags.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import org.k.flags.weather.Weather

class WeatherAPI {

    val API_URL = "https://api.openweathermap.org/data/2.5/weather?"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            })
        }
    }

    @NativeCoroutines
    suspend fun getWeather(lat: Double, lng: Double): Weather {
        return httpClient.get(API_URL + "lat=${lat}&lon=${lng}&appid=${Config.WeatherApiKey}&units=metric").body()
    }
}