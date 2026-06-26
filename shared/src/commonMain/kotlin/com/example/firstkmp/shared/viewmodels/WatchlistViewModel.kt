package com.example.firstkmp.shared.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstkmp.shared.FavoriteCoin
import com.example.firstkmp.shared.PriceInfo
import com.example.firstkmp.shared.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PriceDirection { UP, DOWN, UNCHANGED }

data class WatchlistUiState(
    val prices: Map<String, PriceInfo> = emptyMap(),
    val priceChanges: Map<String, PriceDirection> = emptyMap(),
    val sparklineData: Map<String, List<Double>> = emptyMap(),
    val favorites: List<FavoriteCoin> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WatchlistViewModel(
    private val repository: WatchlistRepository
) : ViewModel() {

    private val coinIds = listOf(
        "bitcoin", "ethereum", "solana", "binancecoin", "ripple",
        "cardano", "dogecoin", "polkadot", "litecoin", "chainlink",
        "avalanche-2", "tron", "stellar", "uniswap", "near",
        "internet-computer", "monero", "aptos", "filecoin", "arbitrum"
    )

    private val priceHistory = mutableMapOf<String, MutableList<Double>>()

    private val _prices = MutableStateFlow<Map<String, PriceInfo>>(emptyMap())
    private val _priceChanges = MutableStateFlow<Map<String, PriceDirection>>(emptyMap())
    private val _sparklineData = MutableStateFlow<Map<String, List<Double>>>(emptyMap())
    private val _favorites = MutableStateFlow<List<FavoriteCoin>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<WatchlistUiState> = combine(
        _prices, _priceChanges, _sparklineData, _favorites, _isLoading
    ) { prices, priceChanges, sparklineData, favorites, isLoading ->
        WatchlistUiState(
            prices = prices,
            priceChanges = priceChanges,
            sparklineData = sparklineData,
            favorites = favorites,
            isLoading = isLoading,
            errorMessage = _errorMessage.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WatchlistUiState()
    )

    init {
        loadPrices()
        viewModelScope.launch {
            repository.getFavorites().collect { favoritesList ->
                _favorites.value = favoritesList
            }
        }
    }

    fun loadPrices() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val previousPrices = _prices.value
                val newPrices = repository.getPrices(coinIds)

                val changes = newPrices.mapValues { (coinId, newPriceInfo) ->
                    val oldPrice = previousPrices[coinId]?.usd
                    when {
                        oldPrice == null -> PriceDirection.UNCHANGED
                        newPriceInfo.usd > oldPrice -> PriceDirection.UP
                        newPriceInfo.usd < oldPrice -> PriceDirection.DOWN
                        else -> PriceDirection.UNCHANGED
                    }
                }

                newPrices.forEach { (coinId, priceInfo) ->
                    val history = priceHistory.getOrPut(coinId) { mutableListOf() }
                    history.add(priceInfo.usd)
                    if (history.size > 10) history.removeAt(0)
                }

                _priceChanges.value = changes
                _sparklineData.value = priceHistory.mapValues { it.value.toList() }
                _prices.value = newPrices
            } catch (e: Exception) {
                _errorMessage.value = "Couldn't load prices. Check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(coinId: String, displayName: String) {
        viewModelScope.launch {
            val isFavorited = _favorites.value.any { it.coinId == coinId }
            if (isFavorited) {
                repository.removeFavorite(FavoriteCoin(coinId, displayName))
            } else {
                repository.addFavorite(FavoriteCoin(coinId, displayName))
            }
        }
    }
}