package org.k.flags.location

import java.util.Locale

class AndroidCountryCodeService() : CountryCodeService {
    override fun getCountryCode(): String? {
        return Locale.getDefault().country
    }
}

actual fun getCountryCodeService(): CountryCodeService = AndroidCountryCodeService()