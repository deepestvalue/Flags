package org.k.flags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import org.k.flags.country.Country
import org.k.flags.country.GeocodeResponse
import coil3.compose.SubcomposeAsyncImage

val MAP_URL = "https://maps.geoapify.com/v1/staticmap"

@Composable
fun CountryMapCard(country: Country, geocode: GeocodeResponse?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Territory of ${country.name.common}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            // 1. Extract the Bounding Box from the response
            val bbox = geocode?.features?.firstOrNull()?.bbox
            if (bbox != null && bbox.size == 4) {
                val mapUrl = getCountryMapUrl(geocode)

                SubcomposeAsyncImage(
                    model = mapUrl,
                    contentDescription = "Map showing the full territory of ${country.name.common}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        // This shows while the IMAGE is downloading
                        Box(
                            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        }
                    },
                    error = {
                        // This shows if the network request fails
                        Box(
                            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Failed to load map", color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                )
            } else {
                // 2. Fallback/Loading State
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (geocode == null) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    } else {
                        Text("Map data unavailable", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

fun getCountryMapUrl(geocode: GeocodeResponse): String {
    val bbox = geocode.features[0].bbox!!
    val placeId = geocode.features.firstOrNull()?.properties?.placeId

    // Geoapify format: rect:min_lon,min_lat,max_lon,max_lat
    val area = "rect:${bbox.joinToString(",")}"

    val mapUrl =  buildString {
        append(MAP_URL)
        append("?style=toner")
        append("&width=800")
        append("&height=500")
        append("&area=$area")

        if (placeId != null) {
            // Use '=' for attributes inside the geometry string
//            val geometry = "place:$placeId;fillColor=#00000033;strokeColor=#000000;strokeWeight=2"
//            // Important: Use encodeURLQueryComponent to handle the # and ; characters
//            append("&geometry=${geometry.encodeURLQueryComponent()}")
        }

        append("&apiKey=${Config.GeoApiKey}")
    }

    Logger.d("Map URL without placeId $mapUrl")
    return mapUrl
}