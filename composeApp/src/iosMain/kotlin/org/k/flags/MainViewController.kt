package org.k.flags

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSHomeDirectory

val cachePath = "${NSHomeDirectory()}/country_cache.json"
fun MainViewController() = ComposeUIViewController { App(cachePath) }