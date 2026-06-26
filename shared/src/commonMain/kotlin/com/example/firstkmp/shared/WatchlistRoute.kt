package com.example.firstkmp.shared

import kotlinx.serialization.Serializable

@Serializable
object WatchlistRoute

@Serializable
data class CoinDetailRoute(val coinId: String, val price: Double)