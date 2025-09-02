package com.example.bulkapp.feature.trade.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.*

import com.example.bulkapp.core.*

@Composable
fun TradeRoute(
    symbol: String,
    onBack: () -> Unit
) {
    TradeScreen(symbol, onBack)
}

@Composable
fun TradeScreen(
    symbol: String,
    onBack: () -> Unit
) {
    val ws = remember { WebSocketDemo() }
    var orderBook by remember { mutableStateOf(OrderBook()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(symbol) {
        runCatching {
            ws.connectMockData(symbol)
                .map(::snapshotToOrderBook)       // rebuild from each snapshot
                .collect { orderBook = it }       // now you’ll have ~20 bid + 20 ask rows
        }.onFailure { e -> error = e.stackTraceToString() }
    }

    Scaffold(
        topBar = {
            MarketTopBar(
                symbol = symbol,
                isPerp = true,
                price = 0.0,        // TODO
                changePct = 0.0,    // TODO
                onBack = onBack,
                onFavorite = { /* TODO */ },
                onShare = { /* TODO */ },
                onAlerts = { /* TODO */ }
            )
        },
        containerColor = Color.Transparent
    ) { inner ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(inner),
            contentAlignment = Alignment.Center
        ) {
            if (error != null) {
                ErrorCard(error!!)
            } else {
                MarketTabs(modifier = Modifier.fillMaxSize(), startIndex = 1,orderBook)
            }
        }
    }
}


