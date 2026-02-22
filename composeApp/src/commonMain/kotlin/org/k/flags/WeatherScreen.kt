package org.k.flags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import org.k.flags.cache.CountrySDK
import org.k.flags.country.Country
import org.k.flags.country.FactbookCountry
import org.k.flags.country.GeocodeResponse
import org.k.flags.network.CountryDetailAPI


@Composable
fun WeatherScreen(countriesCachePath: String,
                  countryOfficialName: String,
                  capital: String,
                  lat: Double,
                  long: Double) {

    Surface {
        val scrollState = rememberScrollState()
        var country: Country? by remember { mutableStateOf(null) }
        var factbookDetails: FactbookCountry? by remember { mutableStateOf(null) }

        // State to hold the geocode data
        var geocodeResponse by remember { mutableStateOf<GeocodeResponse?>(null) }
        var isMapLoading by remember { mutableStateOf(false) }

        LaunchedEffect(countryOfficialName) {
            val allCountries = CountrySDK(countriesCachePath).getCountries()
            country = allCountries.find { it.name.official == countryOfficialName }
        }

        // Fetch the Factbook Details from the API
        LaunchedEffect(country) {
            // 1. Check if country is not null
            // 2. Check if factbookUrl is not null or blank
            country?.factbookUrl?.takeIf { it.isNotBlank() }?.let { url ->
                try {
                    factbookDetails = CountryDetailAPI().getCountrySummary(url)
                } catch (e: Exception) {
                    Logger.e("Factbook API failed: ${e.message}")
                }
            }
        }

        // Fetch once when the screen opens or the country changes
        LaunchedEffect(country) {
            isMapLoading = true
            country?.takeIf { it.name.common.isNotBlank() && it.cca2.isNotBlank() }
                ?.let {
                    geocodeResponse = CountryDetailAPI().getCountryGeocodeResponse(it.name.common, it.cca2)
                }
            isMapLoading = false
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Replaces Spacers
        ) {
            item {
                WeatherCard(
                    modifier = Modifier,
                    cityName = capital,
                    lat = lat,
                    long = long
                )
            }

            country?.let {
                item {
                    CountrySummaryCard(it, factbook = factbookDetails)
                }

                item {
                    CountryMapCard(it, geocodeResponse)
                }
            }
        }
    }
}