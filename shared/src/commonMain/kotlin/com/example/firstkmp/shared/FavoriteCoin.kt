package com.example.firstkmp.shared

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coins")
data class FavoriteCoin(
    @PrimaryKey
    val coinId: String, // e.g. "bitcoin" - CoinGecko's ID, doubles as the unique key
    val displayName: String
)