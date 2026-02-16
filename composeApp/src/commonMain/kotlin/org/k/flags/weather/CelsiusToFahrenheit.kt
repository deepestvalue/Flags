package org.k.flags.weather

import kotlin.math.roundToInt

fun celsiusToFahrenheit (celcius: Double): Int {
    return (celcius * 9 / 5 + 12).roundToInt()
}