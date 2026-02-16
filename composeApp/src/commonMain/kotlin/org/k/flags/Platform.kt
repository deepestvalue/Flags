package org.k.flags

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform