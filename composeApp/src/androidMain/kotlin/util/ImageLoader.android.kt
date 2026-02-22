package util

import okio.Path
import okio.Path.Companion.toPath
import coil3.PlatformContext

actual fun provideCachePath(context: PlatformContext): Path? {
    // No more LocalContext.current error
    return context.cacheDir.absolutePath.toPath()
}