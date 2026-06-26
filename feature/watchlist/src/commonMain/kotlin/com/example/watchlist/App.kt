package com.example.watchlist

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = WatchlistRoute
        ) {
            composable<WatchlistRoute> {
                WatchlistScreen(
                    onCoinClick = { coinId, price ->
                        navController.navigate(CoinDetailRoute(coinId, price))
                    }
                )
            }
            composable<CoinDetailRoute>(
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }) + fadeIn()
                },
                exitTransition = {
                    fadeOut()
                },
                popEnterTransition = {
                    fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                }
            ) { backStackEntry ->
                val route: CoinDetailRoute = backStackEntry.toRoute()
                CoinDetailScreen(
                    coinId = route.coinId,
                    price = route.price,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

