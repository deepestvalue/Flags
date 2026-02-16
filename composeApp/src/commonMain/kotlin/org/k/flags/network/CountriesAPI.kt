package org.k.flags.network

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.k.flags.country.Country

class CountriesAPI {

    // Available fields here https://www.npmjs.com/package/@yusifaliyevpro/countries#available-fields
    val API_URL = "https://restcountries.com/v3.1/all?fields=name,cca2,flags,capital,capitalInfo,languages,currencies,region,subregion"

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
    suspend fun getAllCountries(): List<Country> {
        return httpClient.get(API_URL).body<List<Country>>().sortedBy { it.name.common }
    }
}