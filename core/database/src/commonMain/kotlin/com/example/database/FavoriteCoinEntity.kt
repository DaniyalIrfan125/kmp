package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.FavoriteCoin

@Entity(tableName = "favorite_coins")
data class FavoriteCoinEntity(
    @PrimaryKey
    val coinId: String,
    val displayName: String
)

fun FavoriteCoinEntity.toDomainModel(): FavoriteCoin =
    FavoriteCoin(coinId = coinId, displayName = displayName)

fun FavoriteCoin.toEntity(): FavoriteCoinEntity =
    FavoriteCoinEntity(coinId = coinId, displayName = displayName)