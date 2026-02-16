package org.k.flags.country

import kotlinx.serialization.Serializable

@Serializable
data class Flags(
    val alt: String = "",
    val png: String,
    val svg: String
)
