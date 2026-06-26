package com.example.watchlist

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual fun formatPrice(amount: Double, currencyCode: String): String {
    val formatter = NSNumberFormatter()
    formatter.numberStyle = NSNumberFormatterCurrencyStyle
    formatter.currencyCode = currencyCode

    val number = NSNumber(double = amount)
    return formatter.stringFromNumber(number) ?: amount.toString()
}