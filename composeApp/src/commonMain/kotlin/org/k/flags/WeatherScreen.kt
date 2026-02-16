package org.k.flags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import org.k.flags.network.CountryDetailAPI


@Composable
fun WeatherScreen(countriesCachePath: String, countryName: String, capital: String, lat: Double, long: Double) {

    Surface {
        var country: Country? by remember { mutableStateOf(null) }
        var factbookDetails: FactbookCountry? by remember { mutableStateOf(null) }

        LaunchedEffect(countryName) {
            val allCountries = CountrySDK(countriesCachePath).getCountries()
            country = allCountries.find { it.name.official == countryName }
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

        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            WeatherCard(
                modifier = Modifier,
                cityName = capital,
                lat = lat,
                long = long
            )

            Spacer(modifier = Modifier.height(8.dp))

            country?.let {
                CountrySummaryCard(it, factbook = factbookDetails)
            }
        }
    }
}