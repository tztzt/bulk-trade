package com.example.bulkapp.feature.list.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bulkapp.utils.throttleClick
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ListRoute(
    onOpenTicker: (String) -> Unit
) {
    val items = remember {
        listOf(
            "BTC-USD",
            "ETH-USD",
            "SOL-USD",
            "ADA-USD",
            "DOGE-USD"
        )
    }
    ListScreen(items, onOpenTicker)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreen(
    tickers: List<String>,
    onOpenTicker: (String) -> Unit
) {

    Scaffold(topBar = { TopAppBar(title = { Text("Market Pairs") }) }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(tickers, key = { it }) { symbol ->
                ListItem(
                    headlineContent = { Text(symbol) },
                    modifier = Modifier.throttleClick(600.milliseconds){
                        onOpenTicker(symbol)
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
