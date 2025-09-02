package com.example.bulkapp.core

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

fun mockData(
    coin: String,
    levels: Int = 20,      // how many levels per side
    tick: Double = 0.01,   // price step between levels
    startMid: Double = 155.00
): Flow<L2BookMessage> = flow {
    val rng = Random(7)
    var t = 1672531200000L
    var mid = startMid

    while (currentCoroutineContext().isActive) {
        val bids = genSide(mid, levels, tick, dir = -1, rng = rng) // below mid
        val asks = genSide(mid, levels, tick, dir = +1, rng = rng) // above mid

        // NOTE: use positional args to avoid backtick named args gotchas
        emit(
            L2BookMessage(
                channel = "l2Book:$coin",
                data = L2BookData(coin, bids, asks, t)
            )
        )

        delay(1_000)

        // drift the mid a little so the book moves
        mid += (rng.nextDouble() - 0.5) * tick * 8
        t += 1_000
    }
}

private fun genSide(
    mid: Double,
    levels: Int,
    tick: Double,
    dir: Int,                // -1 = bids, +1 = asks
    rng: Random
): List<OrderLevel> {
    val out = ArrayList<OrderLevel>(levels)
    for (i in 0 until levels) {
        val px = mid + dir * (i + 1) * tick
        val sz = 400.0 + rng.nextDouble() * 1500.0
        val n  = rng.nextInt(1, 6) // 1..5
        out += OrderLevel(px = px.str(2), sz = sz.str(2), n = n)
    }
    return out
}

private fun Double.str(decimals: Int = 2): String {
    val f = 10.0.pow(decimals)
    val v = round(this * f) / f
    return v.toString()
}
