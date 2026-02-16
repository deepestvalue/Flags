package org.k.flags.location

interface CountryCodeService {
    fun getCountryCode(): String?
}

expect fun getCountryCodeService(): CountryCodeService