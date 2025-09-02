package com.example.bulkapp.core

import com.example.bulkapp.core.L2BookMessage
import com.example.bulkapp.core.mockData
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.ktor.client.engine.darwin.Darwin
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.emitAll
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonObject

class WebSocketDemo {

    val client = HttpClient(Darwin) {
        install(WebSockets)
    }

    fun connectMockData(coin: String): Flow<L2BookMessage> = flow {
        emitAll(mockData(coin))
    }

//    fun connectAndSubscribe(coin: String): Flow<L2BookMessage> = flow {
//        client.webSocket("wss://exchange-api.bulk.trade/ws") {
//
//            val subscribeMsg = buildJsonObject {
//                put("method", "subscribe")
//                putJsonObject("subscription") {
//                    put("type", "l2Book")
//                    put("coin", "SOL-USD")
//                }
//            }.toString()
//
//            send(subscribeMsg)
//
//            for (frame in incoming) {
//                when (frame) {
//                    is Frame.Text -> {
//                        val text = frame.readText()
//                        emit(text)
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//    }
}
