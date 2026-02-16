package org.k.flags.country

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FactbookCountry(
    @SerialName("Introduction") val introduction: Introduction? = null,
    @SerialName("Geography") val geography: Geography? = null,
    @SerialName("Economy") val economy: Economy? = null,
    @SerialName("Government") val government: Government? = null,
    @SerialName("People and Society") val peopleAndSociety: PeopleAndSociety? = null
)

@Serializable
data class Introduction(
    @SerialName("Background") val background: TextWrapper? = null
)

@Serializable
data class Geography(
    @SerialName("Location") val location: TextWrapper? = null,
    @SerialName("Terrain") val terrain: TextWrapper? = null,
    @SerialName("Climate") val climate: TextWrapper? = null
)

@Serializable
data class Economy(
    @SerialName("Economic overview") val overview: TextWrapper? = null
)

@Serializable
data class Government(
    @SerialName("Country name") val countryName: CountryNameDetails? = null,
    @SerialName("Capital") val capital: CapitalDetails? = null
)

@Serializable
data class PeopleAndSociety(
    @SerialName("Population") val population: Population? = null,
    @SerialName("Religions") val religions: TextWrapper? = null
)

@Serializable
data class Population(
    @SerialName("total") val total: TextWrapper? = null,
)

// The "Factbook Wrapper" – almost every field uses this
@Serializable
data class TextWrapper(
    val text: String? = null
)

@Serializable
data class CountryNameDetails(
    @SerialName("conventional long form") val longForm: TextWrapper? = null,
    @SerialName("conventional short form") val shortForm: TextWrapper? = null
)

@Serializable
data class CapitalDetails(
    val name: TextWrapper? = null,
    @SerialName("time difference") val timeDifference: TextWrapper? = null
)

// use this function to clean some of the HTML tags that appears in the Factbook text
fun String.cleanFactbookText(): String {
    return this
        // 1. Convert closing paragraphs to double newlines for readability
        .replace("</p>", "\n\n")
        // 2. Convert line breaks to single newlines
        .replace("<br>", "\n")
        .replace("<br/>", "\n")
        // 3. Strip ALL remaining HTML tags (anything between < and >)
        .replace(Regex("<[^>]*>"), "")
        // 4. Decode common HTML entities (if the Factbook uses them)
        .replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .trim()
}
