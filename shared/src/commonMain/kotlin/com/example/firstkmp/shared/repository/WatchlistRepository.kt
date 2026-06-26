package com.example.firstkmp.shared.repository

import com.example.firstkmp.shared.AppDatabase
import com.example.firstkmp.shared.FavoriteCoin
import com.example.firstkmp.shared.fetchCryptoPrices
import com.example.model.PriceInfo
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(
    private val database: AppDatabase
) {
    suspend fun getPrices(coinIds: List<String>): Map<String, PriceInfo> {
        return fetchCryptoPrices(coinIds)
    }

    fun getFavorites(): Flow<List<FavoriteCoin>> {
        return database.favoriteCoinDao().getAllFavorites()
    }

    suspend fun addFavorite(coin: FavoriteCoin) {
        database.favoriteCoinDao().insert(coin)
    }

    suspend fun removeFavorite(coin: FavoriteCoin) {
        database.favoriteCoinDao().delete(coin)
    }
}