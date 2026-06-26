package com.example.watchlist

import com.example.database.AppDatabase
import com.example.database.toDomainModel
import com.example.database.toEntity
import com.example.model.FavoriteCoin
import com.example.model.PriceInfo
import com.example.network.fetchCryptoPrices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepository(
    private val database: AppDatabase
) {
    suspend fun getPrices(coinIds: List<String>): Map<String, PriceInfo> {
        return fetchCryptoPrices(coinIds)
    }

    fun getFavorites(): Flow<List<FavoriteCoin>> {
        return database.favoriteCoinDao().getAllFavorites()
            .map { entities -> entities.map { it.toDomainModel() } }
    }



    suspend fun addFavorite(coin: FavoriteCoin) {
        database.favoriteCoinDao().insert(coin.toEntity())
    }

    suspend fun removeFavorite(coin: FavoriteCoin) {
        database.favoriteCoinDao().delete(coin.toEntity())
    }
}