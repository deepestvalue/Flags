package org.k.flags.network

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.k.flags.Config
import org.k.flags.country.FactbookCountry
import org.k.flags.country.GeocodeResponse

class CountryDetailAPI {

    val GEO_BUNDING_BOX_API_URL = "https://api.geoapify.com/v1/geocode/search"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            },
                // this is needed because the call to github factbook serve raw file as text/plain
                // not application/json
                contentType = ContentType.Any)
        }
    }

    @NativeCoroutines
    suspend fun getCountrySummary(url: String): FactbookCountry? {
        val response = httpClient.get(url)

        if (response.status == HttpStatusCode.OK) {
            return response.body<FactbookCountry>()
        } else {
            Logger.e("Factbook API failed with status: ${response.status} at URL: $url")
            return null
        }
    }

    @NativeCoroutines
    suspend fun getCountryGeocodeResponse(countryName: String, countryCode: String): GeocodeResponse? {
        val response: GeocodeResponse = httpClient.get(GEO_BUNDING_BOX_API_URL) {
            parameter("text", countryName)
            parameter("type", "country")
            parameter("filter", "countrycode:${countryCode.lowercase()}")
            parameter("limit", "1")
            parameter("apiKey", Config.GeoApiKey)
        }.body<GeocodeResponse>()

        return response
    }

}
