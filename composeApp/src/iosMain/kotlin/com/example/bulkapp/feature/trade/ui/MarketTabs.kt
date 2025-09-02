@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.bulkapp.feature.trade.ui

import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bulkapp.core.L2BookMessage
import com.example.bulkapp.core.OrderBook
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun MarketTabs(
    modifier: Modifier = Modifier,
    startIndex: Int = 1, // default to "Orderbook"
    book: OrderBook,
) {
    val tabs = listOf("Chart", "Orderbook", "Depth", "Trades")
    var selected by rememberSaveable { mutableIntStateOf(startIndex.coerceIn(0, tabs.lastIndex)) }
    val json = remember { Json { prettyPrint = true } }

    val container = Color(0xFF171510)
    val selectedColor = Color(0xFFEDE4DD)
    val unselectedColor = Color(0xFFB6ABA4)
    val divider = Color(0xFF3B3732)
    val indicator = Color(0xFFF6B545)

    Column(modifier.background(container)) {
        TabRow(
            selectedTabIndex = selected,
            containerColor = container,
            contentColor = selectedColor,
            divider = { HorizontalDivider(color = divider) },
            indicator = { positions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(positions[selected])
                        .height(3.dp)
                        .background(indicator)
                )
            }
        ) {
            tabs.forEachIndexed { i, title ->
                CompactTab(
                    label = title,
                    selected = selected == i,
                    onClick = { selected = i },
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor
                )
            }
        }

        HorizontalDivider(color = divider)

        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {

            // Conditional render
            when (selected) {
                0 -> Text(tabs[0])
                1 -> { OrderBookView(book)}
                2 -> Text(tabs[2])
                3 -> Text(tabs[3])
            }
        }
    }
}

@Composable
private fun CompactTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
) {
    val textColor = if (selected) selectedColor else unselectedColor
    Box(
        modifier = Modifier
            .padding(horizontal = 0.dp)
            .height(40.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            fontSize = 14.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            color = textColor
        )
    }
}

//Column(
//horizontalAlignment = Alignment.CenterHorizontally,
//verticalArrangement = Arrangement.Top,
//modifier = Modifier.fillMaxSize()
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxWidth()
//            .weight(1f)
//            .clip(RoundedCornerShape(16.dp))
//            .background(MaterialTheme.colorScheme.surfaceVariant)
//            .padding(8.dp)
//    ) {
//        items(messages) { msg ->
//            val pretty = remember(msg) { json.encodeToString(msg) }
//            MessageRow(pretty)
//        }
//    }
//}