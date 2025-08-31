package com.example.bulkapp

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import io.ktor.client.engine.darwin.Darwin


class WebSocketDemo {

    val client = HttpClient(Darwin) {
        install(WebSockets)
    }

    fun connectAndSubscribe(): Flow<String> = flow {
        client.webSocket("wss://exchange-api.bulk.trade/ws") {

            val subscribeMsg = buildJsonObject {
                put("method", "subscribe")
                putJsonObject("subscription") {
                    put("type", "l2Book")
                    put("coin", "SOL-USD")
                }
            }.toString()

            send(subscribeMsg)

            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        emit(text)
                    }
                    else -> {}
                }
            }
        }
    }
}
