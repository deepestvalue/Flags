package util

import coil3.ImageLoader
import coil3.PlatformContext
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

// Expect function to get the platform-specific cache directory
expect fun provideCachePath(context: PlatformContext): Path?