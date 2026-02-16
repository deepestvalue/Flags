package org.k.flags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.k.flags.cache.CountrySDK
import org.k.flags.country.Country
import kotlin.text.contains

@Composable
fun HomeScreen(countriesCachePath: String,
               searchQuery: String,
               navigateToWeather: (String, String, Double, Double) -> Unit) {
    Surface {
        var listCountries: List<Country> by remember { mutableStateOf(mutableListOf()) }

        LaunchedEffect(Unit) {
            listCountries = CountrySDK(countriesCachePath).getCountries()
        }

        val filteredList = remember(searchQuery, listCountries) {
            if (searchQuery.isBlank()) {
                listCountries
            } else {
                listCountries.filter {
                    it.name.common.contains(searchQuery, ignoreCase = true) ||
                        it.name.official.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        Column {
            LazyColumn {
                itemsIndexed(items = filteredList) { index, item ->
                    CountryCard(
                        modifier =  Modifier.fillMaxWidth(),
                        country = item,
                        currentCountry = index == 0,
                        navigateToWeather
                    )
                }
            }
        }
    }
}