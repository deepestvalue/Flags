package org.k.flags.country

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val capital: List<String> = emptyList(),
    val capitalInfo: CapitalInfo? = null,
    val flags: Flags,
    val name: Name,
    val cca2: String,
    val region: String,
    val subregion: String,
    val currencies: Map<String, CurrencyDetail> = emptyMap(),
    val languages: Map<String, String> = emptyMap(),
    val factbookUrl: String? = null
)
