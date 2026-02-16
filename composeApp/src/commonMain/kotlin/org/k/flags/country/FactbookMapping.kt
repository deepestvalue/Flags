package org.k.flags.country

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FactbookMapping(
    @SerialName("g") val gec: String? = "",
    @SerialName("r") val region: String? = "",
    @SerialName("n") val name: String? = ""
)
