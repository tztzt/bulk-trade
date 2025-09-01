package com.example.bulkapp.feature.trade.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.compose.*

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

    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) darkColorScheme() else lightColorScheme()
    ) {
        // Surface provides background + content color (onBackground), fixing the black-on-black issue
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Box(
                Modifier
                    .fillMaxSize()
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
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // RTL-aware
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            symbol,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(16.dp))

                        // Give the list a contrasting surface and rounded shape
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
}


