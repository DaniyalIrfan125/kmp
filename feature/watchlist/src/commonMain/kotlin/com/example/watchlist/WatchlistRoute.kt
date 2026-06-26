package com.example.watchlist

import kotlinx.serialization.Serializable

@Serializable
object WatchlistRoute

@Serializable
data class CoinDetailRoute(val coinId: String, val price: Double)