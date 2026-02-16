package org.k.flags.cache

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import org.k.flags.country.Country
import org.k.flags.location.getCountryCodeService
import org.k.flags.network.CountryAPI
import kotlinx.io.files.Path

class CountrySDK (countriesCachePath: String) {
    private val cache: KStore<List<Country>> = storeOf(file = Path(countriesCachePath))

    private val api: CountryAPI = CountryAPI()

    @NativeCoroutines
    suspend fun getCountries(): List<Country> {
        val countryCode = getCountryCodeService().getCountryCode()

        val tempCountries = getStoredCountries().toMutableList()
        val currentCountry = tempCountries.first {it.cca2 == countryCode}
        // add user's country on top
        tempCountries.remove(currentCountry)
        tempCountries.add(0, currentCountry)

        return tempCountries
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
            cache.delete()
            return emptyList()
        }
    }


}

//expect fun pathToCountryCache(): String