package com.example.firstkmp.shared


import kotlinx.serialization.Serializable

@Serializable
data class `PriceInfo`(
    val usd: Double
)

// CoinGecko returns: { "bitcoin": { "usd": 43251.18 }, "ethereum": { "usd": 2891.42 } }
// The outer object's keys are dynamic (coin IDs), so we can't model it as a fixed data class.
// kotlinx.serialization can deserialize directly into a Map<String, PriceInfo> for this shape.
typealias CryptoPriceResponse = Map<String, PriceInfo>