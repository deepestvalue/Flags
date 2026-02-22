package org.k.flags.country

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GeocodeResponse(
    val features: List<GeocodeFeature>
)

@Serializable
data class GeocodeFeature(
    val bbox: List<Double>? = null, // [min_lon, min_lat, max_lon, max_lat]
    val properties: GeocodeProperties
)

@Serializable
data class GeocodeProperties(
    val name: String? = null,
    val country: String? = null,
    @SerialName("place_id") val placeId: String? = null,
    val formatted: String? = null
)
