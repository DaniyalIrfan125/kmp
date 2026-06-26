package com.example.network

import com.example.model.PriceInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println("Ktor: $message")
            }
        }
        level = LogLevel.INFO
    }
}

suspend fun fetchCryptoPrices(coinIds: List<String>): Map<String, PriceInfo> {
    val response = httpClient.get("https://api.coingecko.com/api/v3/simple/price") {
        parameter("ids", coinIds.joinToString(","))
        parameter("vs_currencies", "usd")
    }
    val rawBody = response.bodyAsText()
    println("Ktor raw response: $rawBody")
    return Json { ignoreUnknownKeys = true }.decodeFromString(rawBody)
}