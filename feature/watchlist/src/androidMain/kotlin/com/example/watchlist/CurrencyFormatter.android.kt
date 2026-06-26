package com.example.watchlist

import java.text.NumberFormat
import java.util.Currency

actual fun formatPrice(amount: Double, currencyCode: String): String {
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.currency = Currency.getInstance(currencyCode)
    return formatter.format(amount)
}