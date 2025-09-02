package com.example.bulkapp

import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.serialization.Serializable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.example.bulkapp.feature.list.ui.ListRoute
import com.example.bulkapp.feature.trade.ui.TradeRoute

@Serializable
data class TradeArgs(val symbol: String)

@Composable
fun App() {

    val navController = rememberNavController()
    val dark = isSystemInDarkTheme()

    MaterialTheme (
        colorScheme = if (dark) darkColorScheme() else lightColorScheme()
    ){
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(navController = navController, startDestination = "list") {
                composable("list") {
                    ListRoute(
                        onOpenTicker = { symbol -> navController.navigate(TradeArgs(symbol)) }
                    )
                }
                composable<TradeArgs> { entry ->
                    val args = entry.toRoute<TradeArgs>()
                    TradeRoute(symbol = args.symbol, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}