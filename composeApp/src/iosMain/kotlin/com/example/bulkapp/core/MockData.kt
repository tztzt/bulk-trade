package com.example.bulkapp.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun mockData(): Flow<L2BookMessage> = flow {
    val mockMessages = listOf(
        L2BookMessage(
            channel = "l2Book:SOL-USD",
            data = L2BookData(
                instrument_id = "SOL-USD",
                `0` = listOf(OrderLevel("200.50", "1.5", 1)),
                `1` = listOf(OrderLevel("199.00", "2.0", 2)),
                timestamp = 1672531200000
            )
        ),
        L2BookMessage(
            channel = "l2Book:SOL-USD",
            data = L2BookData(
                instrument_id = "SOL-USD",
                `0` = listOf(OrderLevel("201.00", "0.7", 3)),
                `1` = listOf(OrderLevel("198.75", "1.1", 4)),
                timestamp = 1672531260000
            )
        )
    )

    for (msg in mockMessages) {
        emit(msg)
    }
}
