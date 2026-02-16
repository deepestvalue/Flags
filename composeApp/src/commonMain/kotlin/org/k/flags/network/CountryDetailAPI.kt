package org.k.flags.network

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.k.flags.country.FactbookCountry

class CountryDetailAPI {

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

}