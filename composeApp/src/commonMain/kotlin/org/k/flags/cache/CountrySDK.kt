package org.k.flags.cache

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import org.k.flags.country.Country
import org.k.flags.location.getCountryCodeService
import org.k.flags.network.CountriesAPI
import kotlinx.io.files.Path
import org.k.flags.country.FactbookMapping
import org.k.flags.generated.resources.Res
import kotlinx.serialization.json.Json

class CountrySDK (countriesCachePath: String) {
    private val cache: KStore<List<Country>> = storeOf(file = Path(countriesCachePath))

    private val api: CountriesAPI = CountriesAPI()
    // Memory cache for the GEC/Region mapping
    private var mapping: Map<String, FactbookMapping> = emptyMap()

    // https://github.com/factbook/factbook.json this is the factbook repo that holds
    // countries details in each region
    private val manualOverrides = mapOf(
        "dr congo" to "drc",
        "democratic republic of the congo" to "drc",
        "myanmar" to "burma",
        "vatican city" to "holy see vatican city",
        "palestine" to "west bank", // Factbook uses West Bank or Gaza Strip
        "cape verde" to "cabo verde",
        "turkiye" to "turkey",
        "french guiana" to "france",
        "guadeloupe" to "france",
        "martinique" to "france",
        "mayotte" to "france",
        "reunion" to "france",
        "saint barthelemy" to "france",
        "saint martin" to "france",
        "south georgia" to "south georgia and south sandwich islands",
        "united states virgin islands" to "virgin islands",
        "svalbard and jan mayen" to "jan mayen", // Or svalbard
        "curacao" to "curacao",
        "caribbean netherlands" to "netherlands",
        "saint helena ascension and tristan da cunha" to "saint helena ascension and tristan da cunha",
        "united states minor outlying islands" to "wake island", // Or wake island/midway
        "british indian ocean territory" to "british indian ocean territory",
        "united arab emirates" to "united arab emirates",
        "ivory coast" to "cote divoire",
        "british virgin islands" to "british virgin islands",
        "dominican republic" to "dominica",
        "swaziland" to "eswatini",
        "falkland islands" to "falkland islands (islas malvinas)",
        "micronesia" to "federated states of micronesia",
        "republic of congo" to "congo (brazzaville)",
        "vatican city" to "holy see (vatican city)",
        "aland islands" to "finland"
    )

    fun String.normalizeCountryName(): String {
        return this.lowercase()
            // 1. Strip Accents/Diacritics manually for KMP
            .replace("ç", "c")
            .replace("ã", "a").replace("á", "a").replace("å", "a")
            .replace("é", "e").replace("í", "i").replace("ó", "o")
            .replace("ô", "o").replace("ú", "u").replace("ñ", "n")
            .replace("í", "i") // specifically for Príncipe
            // 2. Standardize punctuation
            .replace("the ", "")
            .replace(",", "")
            .replace(".", "")
            .replace("-", " ")
            .replace("'", "") // Handles Côte d'Ivoire -> cote d'ivoire -> cote divoire
            .replace(Regex("\\s+"), " ")
            .trim()
    }


    @NativeCoroutines
    suspend fun getCountries(): List<Country> {
        if (mapping.isEmpty()) {
            loadMapping()
        }

        val countryCode = getCountryCodeService().getCountryCode()

        val tempCountries = getStoredCountries().toMutableList()

        // add factbook url
        val enrichedCountries = tempCountries.map { country ->
            // find a match between the countries from factbook.json saved in _countries_codes.json
            // and the countries from the cache retrieved from restcountries.com
            var key = country.name.common.normalizeCountryName()
            key = manualOverrides[key] ?: key
            val factbookMatch = mapping[key]
            if (factbookMatch != null) {
                country.copy(factbookUrl = buildCountryFactbookUrl(factbookMatch))
            } else {
                Logger.i("Couldn't find region mapping for Country ${country.name.common.lowercase()}")
                Logger.i("Factbook Mapping Failed: SearchKey was '$key'")
                country
            }
        }.toMutableList()

        val currentCountry = enrichedCountries.first {it.cca2 == countryCode}
        // add user's country on top
        enrichedCountries.remove(currentCountry)
        enrichedCountries.add(0, currentCountry)

        return enrichedCountries
    }

    private suspend fun getStoredCountries(): List<Country> {
        try {
            val cached = cache.get()
            return if (cached.isNullOrEmpty()) {
                api.getAllCountries().also { cache.set(it) }
            } else {
                cached
            }
        } catch (e: Exception) {
            Logger.e("Error loading countries mapping: ${e.message}")

            cache.delete()
            return emptyList()
        }
    }

    private suspend fun loadMapping() {
        val json = Json {
            ignoreUnknownKeys = true // This prevents the crash you just saw
            isLenient = true
        }

        val bytes = Res.readBytes("files/_countries_codes.json")
        mapping = json.decodeFromString(bytes.decodeToString())
    }

    private fun buildCountryFactbookUrl(m: FactbookMapping): String {
        return "https://raw.githubusercontent.com/factbook/factbook.json/master/${m.region?.lowercase()}/${m.gec?.lowercase()}.json"
    }

}
