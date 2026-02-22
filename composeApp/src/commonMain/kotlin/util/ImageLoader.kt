package util

import co.touchlab.kermit.Logger
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import okio.Path

fun getSharedImageLoader(
    context: PlatformContext,
): ImageLoader {
    return ImageLoader.Builder(context)
        .components { add(KtorNetworkFetcherFactory()) }
        .diskCache {
            provideCachePath(context)?.let { path ->
                DiskCache.Builder()
                    .directory(path)
                    .maxSizeBytes(100L * 1024 * 1024) // 100MB
                    .build()
            }
        }
        .build()
}

fun clearImageCache(context: PlatformContext) {
    val imageLoader = SingletonImageLoader.get(context)

    // Clear both RAM and Disk
    imageLoader.memoryCache?.clear()
    imageLoader.diskCache?.clear()

    Logger.d { "Image cache cleared successfully" }
}

// Expect function to get the platform-specific cache directory
expect fun provideCachePath(context: PlatformContext): Path?