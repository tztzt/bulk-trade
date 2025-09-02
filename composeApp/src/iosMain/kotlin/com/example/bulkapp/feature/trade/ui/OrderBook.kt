package com.example.bulkapp.feature.trade.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bulkapp.core.OrderBook
import com.example.bulkapp.utils.toPercent
import kotlin.math.max

data class RowItem(val price: Double, val size: Double, val count: Int)

private val Green = Color(0xFF20C997)
private val GreenBg = Color(0x3320C997)
private val Red = Color(0xFFFF6B6B)
private val RedBg = Color(0x33FF6B6B)
private val Dim = Color(0xFFB0B0B0)

private val headerStyle = TextStyle(fontSize = 12.sp, color = Dim)
private val cellStyle =
    TextStyle(fontSize = 12.sp, color = Color.White, fontFamily = FontFamily.Monospace)

fun OrderBook.top(depth: Int = 16): Pair<List<RowItem>, List<RowItem>> {
    val bidRows = bids.entries
        .sortedByDescending { it.key }
        .take(depth)
        .map { RowItem(it.key, it.value.size, it.value.count) }

    val askRows = asks.entries
        .sortedBy { it.key }
        .take(depth)
        .map { RowItem(it.key, it.value.size, it.value.count) }

    return bidRows to askRows
}

fun OrderBook.bidAskPercents(): Pair<Float, Float> {
    val b = bids.values.sumOf { it.size }
    val a = asks.values.sumOf { it.size }
    val t = (a + b).takeIf { it > 0.0 } ?: 1.0
    return (b / t).toFloat() to (a / t).toFloat()
}
@Composable
fun OrderBookView(book: OrderBook, depth: Int = 16) {
    val (bids, asks) = book.top(depth)
    val maxSize = maxOf(bids.maxOfOrNull { it.size } ?: 0.0, asks.maxOfOrNull { it.size } ?: 0.0)

    Column(Modifier.fillMaxWidth().padding(horizontal = 0.dp)) {
        OrderBookHeaderBar(book)
        HorizontalDivider(thickness = 0.5.dp, color = Color(0x22FFFFFF))
        HeaderRow()
        val rows = max(bids.size, asks.size)
        LazyColumn {
            items(rows) { i ->
                BookRow(
                    bid = bids.getOrNull(i),
                    ask = asks.getOrNull(i),
                    maxSize = maxSize
                )
            }
        }
    }
}

@Composable
fun OrderBookHeaderBar(book: OrderBook) {
    val (bp, ap) = book.bidAskPercents()
    val left  = bp.coerceIn(0f, 1f)
    val right = ap.coerceIn(0f, 1f)

    Row (Modifier.fillMaxWidth()
        .padding(vertical = 8.dp)
        ,horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically){
        Text("B", style = headerStyle)
        Text(left.toDouble().toPercent(2), style = headerStyle.copy(color = Green), softWrap = false)
        // the bar in the middle
        Box(
            Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0x22FFFFFF))
        ) {
            if (left > 0f) {
                Box(
                    Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .fillMaxWidth(left)
                        .background(Green)
                )
            }
            if (right > 0f) {
                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .fillMaxWidth(right)
                        .background(Red)
                )
            }
        }
        Text(right.toDouble().toPercent(2), style = headerStyle.copy(color = Red), softWrap = false)
        Text("S", style = headerStyle)
    }
}
@Composable
private fun HeaderRow() {
    Row(
        Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        // Bids labels
        Row(Modifier.weight(1f).padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Size", style = headerStyle, textAlign = TextAlign.Start)
            Text("Bids", style = headerStyle, textAlign = TextAlign.End)
        }
        // Asks labels
        Row(Modifier.weight(1f).padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Asks", style = headerStyle, textAlign = TextAlign.Start)
            Text("Size", style = headerStyle, textAlign = TextAlign.End)
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color(0x22FFFFFF))
}

@Composable
private fun BookRow(bid: RowItem?, ask: RowItem?, maxSize: Double) {
    Row(Modifier.fillMaxWidth().height(24.dp)) {
        // Bids
        Box(Modifier.weight(1f).fillMaxHeight()) {
            bid?.let {
                val frac = if (maxSize == 0.0) 0f else (it.size / maxSize).toFloat()

                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .fillMaxWidth(frac)
                        .background(GreenBg)
                )

                Row(
                    Modifier.fillMaxWidth().padding(start = 0.dp, top=4.dp,end = 4.dp,bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it.size.toString(), style = cellStyle, textAlign = TextAlign.Start)
                    Text(it.price.toString(), style = cellStyle.copy(color = Green), textAlign = TextAlign.End)
                }
            }
        }
        // Asks
        Box(Modifier.weight(1f).fillMaxHeight()) {
            ask?.let {
                val frac = if (maxSize == 0.0) 0f else (it.size / maxSize).toFloat()

                Box(
                    Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .fillMaxWidth(frac)
                        .background(RedBg)
                )

                Row(
                    Modifier.fillMaxWidth().padding(start = 4.dp, top=4.dp,end = 0.dp,bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it.price.toString(), style = cellStyle.copy(color = Red), textAlign = TextAlign.Start)
                    Text(it.size.toString(), style = cellStyle, textAlign = TextAlign.End)
                }
            }
        }
    }
}