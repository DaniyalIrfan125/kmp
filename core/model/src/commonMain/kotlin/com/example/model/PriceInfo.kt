package com.example.model


import kotlinx.serialization.Serializable

@Serializable
data class PriceInfo(
    val usd: Double
)