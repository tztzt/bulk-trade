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
