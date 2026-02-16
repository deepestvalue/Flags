package org.k.flags

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.k.flags.country.Country

@Composable
fun CountryCard(
    modifier: Modifier,
    country: Country,
    currentCountry: Boolean,
    navigateToWeather: (String, Double, Double) -> Unit
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(4.dp)
        .clickable {
            val capital = country.capital.firstOrNull() ?: ""
            val lat = country.capitalInfo?.latlng?.getOrNull(0) ?: 0.0
            val lng = country.capitalInfo?.latlng?.getOrNull(1) ?: 0.0
            navigateToWeather(capital, lat, lng)
        }
    ) {
        if (currentCountry) {
            Box(modifier = Modifier.fillMaxWidth().border(5.dp, Color.DarkGray)) {
                Country(Modifier.fillMaxWidth(), country, navigateToWeather)
            }
        } else {
            Country(Modifier.fillMaxWidth(), country, navigateToWeather)
        }
    }
}