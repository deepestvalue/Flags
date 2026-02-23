package org.k.flags

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

fun getCachePath(): String {
    val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true)
    val cacheDirectory = paths.first() as String
    return "$cacheDirectory/country_cache.json"
}

fun MainViewController() = ComposeUIViewController {
    // Initialize the path here so it's generated correctly at runtime
    val cachePath = getCachePath()
    App(cachePath)
}