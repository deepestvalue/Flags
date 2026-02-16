package org.k.flags

import android.app.Application
import org.k.flags.cache.filePath

class FlagsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        filePath = "${filesDir.path}/country_cache.json"
    }
}