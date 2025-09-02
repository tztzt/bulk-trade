package com.example.bulkapp.feature.trade.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import com.example.bulkapp.core.WebSocketDemo
import com.example.bulkapp.core.L2BookMessage


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
    val json = remember { Json { prettyPrint = true } }
    val messages = remember { mutableStateListOf<L2BookMessage>() }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(ws) {
        runCatching {
            ws.connectAndSubscribe().collect { messages.add(it) }
        }.onFailure { e ->
            error = e.stackTraceToString()
        }
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
                .padding(inner)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (error != null) {
                ErrorCard(error!!)
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    ) {
                        items(messages) { msg ->
                            val pretty = remember(msg) { json.encodeToString(msg) }
                            MessageRow(pretty)
                        }
                    }
                }
            }
        }
    }
}


