package com.example.firstkmp.shared

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCoinDao {
    @Query("SELECT * FROM favorite_coins")
    fun getAllFavorites(): Flow<List<FavoriteCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: FavoriteCoin)

    @Delete
    suspend fun delete(coin: FavoriteCoin)
}