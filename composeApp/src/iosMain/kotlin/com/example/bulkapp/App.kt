package com.example.bulkapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun App() {
    val ws = remember { WebSocketDemo() }
    val scope = rememberCoroutineScope()

    val messages = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        scope.launch {
            ws.connectAndSubscribe().collect { message ->
                messages.add(message)
            }
        }
    }

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("WebSocket Messages", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // fixed height so it doesn't expand infinitely
                ) {
                    items(messages) { msg ->
                        Text(msg, style = MaterialTheme.typography.bodyMedium)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
