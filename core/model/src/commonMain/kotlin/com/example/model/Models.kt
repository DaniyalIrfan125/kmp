package com.example.model

enum class PriceDirection { UP, DOWN, UNCHANGED }

sealed class AuthResult {
    object Success : AuthResult()
    object Failed : AuthResult()
    data class Error(val message: String) : AuthResult()
}