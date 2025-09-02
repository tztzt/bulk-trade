package com.example.bulkapp.core

import kotlinx.serialization.Serializable

@Serializable
data class L2BookMessage(
    val channel: String,
    val data: L2BookData
)

@Serializable
data class L2BookData(
    val instrument_id: String,
    val `0`: List<OrderLevel>,
    val `1`: List<OrderLevel>,
    val timestamp: Long
)

@Serializable
data class OrderLevel(
    val px: String, // price
    val sz: String, // size
    val n: Int      // level/order count
)
data class OrderBook(
    val instrumentId: String? = null,
    val bids: Map<Double, Level> = emptyMap(), // price -> Level
    val asks: Map<Double, Level> = emptyMap(),
    val lastUpdate: Long = 0L
)

data class Level(val size: Double, val count: Int)

private fun String.d(): Double = toDoubleOrNull() ?: 0.0

private fun List<OrderLevel>.toSide(): Map<Double, Level> {
    val m = mutableMapOf<Double, Level>()
    for (lvl in this) {
        val p = lvl.px.d()
        val s = lvl.sz.d()
        if (p == 0.0 || s <= 0.0) continue
        val prev = m[p]
        m[p] = if (prev == null) Level(s, lvl.n)
        else Level(prev.size + s, prev.count + lvl.n) // combine dup prices in same snapshot
    }
    return m
}

 fun snapshotToOrderBook(msg: L2BookMessage): OrderBook =
    OrderBook(
        instrumentId = msg.data.instrument_id,
        bids = msg.data.`0`.toSide(),
        asks = msg.data.`1`.toSide(),
        lastUpdate = msg.data.timestamp
    )
