package org.k.flags.country

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDetail (
    val name: String,
    val symbol: String
)
