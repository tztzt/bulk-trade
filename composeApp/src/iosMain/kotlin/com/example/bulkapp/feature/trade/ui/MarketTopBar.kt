package com.example.bulkapp.feature.trade.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bulkapp.utils.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketTopBar(
    symbol: String,
    price: Double,
    changePct: Double,
    isPerp: Boolean,
    onBack: () -> Unit,
    onFavorite: () -> Unit,
    onShare: () -> Unit,
    onAlerts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = Color(0xFF171510)       // dark brown/black
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = container,
            navigationIconContentColor = iconTint,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = iconTint
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Column(Modifier.padding(vertical = 2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = symbol,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    if (isPerp) Pill()

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Market type",
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.height(2.dp))

                // Use red like the screenshot; switch to green for positives if you prefer.
                val priceColor = MaterialTheme.colorScheme.error
                Text(
                    text = "${price.toFixed(2)} ${changePct.toSignedPercent(1)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = priceColor
                )
            }
        },
        actions = {
            // Outlined square star
            OutlinedIconButton(
                onClick = onFavorite,
                border = BorderStroke(1.dp, iconTint),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Outlined.Star, contentDescription = "Favorite")
            }
            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onShare) {
                Icon(Icons.Outlined.Share, contentDescription = "Share")
            }
            IconButton(onClick = onAlerts) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Alerts")
            }
        }
    )
}

@Composable
private fun Pill() {
    val bg = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val fg = MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        color = bg,
        contentColor = fg,
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Perp", style = MaterialTheme.typography.labelSmall)
        }
    }
}
