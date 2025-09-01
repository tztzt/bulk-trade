package com.example.bulkapp.core

import com.example.bulkapp.core.L2BookMessage
import com.example.bulkapp.core.mockData
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.ktor.client.engine.darwin.Darwin

class WebSocketDemo {

    val client = HttpClient(Darwin) {
        install(WebSockets)
    }


    fun connectAndSubscribe(): Flow<L2BookMessage> = flow {
        mockData().collect { message ->
            emit(message)
        }
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
//                    else -> {}
//                }
//            }
//        }
    }
}
