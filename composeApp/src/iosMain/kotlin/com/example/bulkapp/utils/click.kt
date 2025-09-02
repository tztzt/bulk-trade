package com.example.bulkapp.utils

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.foundation.clickable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

fun Modifier.throttleClick(
    window: Duration = 600.milliseconds,
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    var lastClick: TimeMark? by remember { mutableStateOf(null) }

    Modifier.clickable(enabled = enabled) {
        val elapsed = lastClick?.elapsedNow() ?: Duration.INFINITE
        if (elapsed >= window) {
            lastClick = TimeSource.Monotonic.markNow()
            onClick()
        }
    }
}
