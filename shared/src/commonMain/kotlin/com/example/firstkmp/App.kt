package com.example.firstkmp

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.firstkmp.shared.CoinDetailRoute
import com.example.firstkmp.shared.CoinDetailScreen
import com.example.firstkmp.shared.WatchlistRoute
import com.example.firstkmp.shared.formatPrice
import com.example.firstkmp.shared.viewmodels.WatchlistViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = WatchlistRoute
        ) {
            composable<WatchlistRoute> {
                WatchlistScreen(
                    onCoinClick = { coinId, price ->
                        navController.navigate(CoinDetailRoute(coinId, price))
                    }
                )
            }
            composable<CoinDetailRoute>(
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it }) + fadeIn()
                },
                exitTransition = {
                    fadeOut()
                },
                popEnterTransition = {
                    fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                }
            ) { backStackEntry ->
                val route: CoinDetailRoute = backStackEntry.toRoute()
                CoinDetailScreen(
                    coinId = route.coinId,
                    price = route.price,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//@Preview
//fun App() {
//    MaterialTheme {
//        val viewModel = koinViewModel<WatchlistViewModel>()
//        val prices by viewModel.prices.collectAsState()
//        val favorites by viewModel.favorites.collectAsState()
//        val isLoading by viewModel.isLoading.collectAsState()
//        val errorMessage by viewModel.errorMessage.collectAsState()
//
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Column(
//                modifier = Modifier
//                    .safeContentPadding()
//                    .fillMaxSize()
//            ) {
//                Text(
//                    text = "Watchlist",
//                    style = MaterialTheme.typography.headlineMedium,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
//                )
//
//                PullToRefreshBox(
//                    isRefreshing = isLoading,
//                    onRefresh = { viewModel.loadPrices() },
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    when {
//                        errorMessage != null && prices.isEmpty() -> {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(32.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Outlined.CloudOff,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(48.dp),
//                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//                                Spacer(modifier = Modifier.height(12.dp))
//                                Text(
//                                    text = errorMessage ?: "Something went wrong",
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                                    textAlign = TextAlign.Center
//                                )
//                                Spacer(modifier = Modifier.height(16.dp))
//                                Button(onClick = { viewModel.loadPrices() }) {
//                                    Text("Retry")
//                                }
//                            }
//                        }
//                        else -> {
//                            LazyColumn(
//                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//                                verticalArrangement = Arrangement.spacedBy(10.dp)
//                            ) {
//                                items(prices.entries.toList()) { (coinId, priceInfo) ->
//                                    val isFavorited = favorites.any { it.coinId == coinId }
//                                    CoinRow(
//                                        coinId = coinId,
//                                        price = priceInfo.usd,
//                                        isFavorited = isFavorited,
//                                        onToggleFavorite = { viewModel.toggleFavorite(coinId, coinId) }
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CoinRow(
//    coinId: String,
//    price: Double,
//    isFavorited: Boolean,
//    onToggleFavorite: () -> Unit
//) {
//    val displayName = coinId
//        .split("-")
//        .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 14.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.primaryContainer),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = displayName.take(1),
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                }
//                Spacer(modifier = Modifier.width(12.dp))
//                Column {
//                    Text(
//                        text = displayName,
//                        style = MaterialTheme.typography.bodyLarge,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Text(
//                        text = formatPrice(price, "USD"),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//
//            IconButton(onClick = onToggleFavorite) {
//                Icon(
//                    imageVector = if (isFavorited) Icons.Filled.Star else Icons.Outlined.StarBorder,
//                    contentDescription = if (isFavorited) "Remove from favorites" else "Add to favorites",
//                    tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}