package com.example.bulkapp.utils

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round

/**
 * Formats a Double with fixed decimal places, no locale/Java deps.
 * Handles rounding and avoids "-0.00".
 *
 * @param decimals 0..9 recommended
 */
fun Double.toFixed(decimals: Int): String {
    require(decimals >= 0 && decimals <= 9) { "decimals must be 0..9" }

    val negative = this < 0.0
    val pDouble = 10.0.pow(decimals)
    val pLong = pDouble.toLong()

    // scale, round, and work in integers to keep trailing zeros reliable
    val scaled = round(abs(this) * pDouble).toLong()
    val intPart = scaled / pLong
    val fracPart = scaled % pLong

    val sign = if (negative && (intPart != 0L || fracPart != 0L)) "-" else "" // avoid "-0.00"

    return if (decimals == 0) {
        "$sign$intPart"
    } else {
        val fracStr = fracPart.toString().padStart(decimals, '0')
        "$sign$intPart.$fracStr"
    }
}

/** Adds explicit +/− sign. */
fun Double.toSigned(decimals: Int): String =
    (if (this >= 0.0) "+" else "-") + abs(this).toFixed(decimals)

/** Signed percent with % suffix. */
fun Double.toSignedPercent(decimals: Int): String =
    this.toSigned(decimals) + "%"


/** Signed percent with % suffix. */
fun Double.toPercent(decimals: Int = 2, showSign: Boolean = false): String {
    val scaled = if (abs(this) <= 1.0 + 1e-9) this * 100.0 else this
    return if (showSign) scaled.toSigned(decimals) + "%" else scaled.toFixed(decimals) + "%"
}
