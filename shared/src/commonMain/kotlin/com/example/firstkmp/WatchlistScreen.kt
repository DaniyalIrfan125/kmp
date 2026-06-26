package com.example.firstkmp


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.firstkmp.shared.formatPrice
import com.example.firstkmp.shared.viewmodels.PriceDirection
import com.example.firstkmp.shared.viewmodels.WatchlistViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(onCoinClick: (coinId: String, price: Double) -> Unit) {
    val viewModel = koinViewModel<WatchlistViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.safeContentPadding().fillMaxSize()) {
            Text(
                text = "Watchlist",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = { viewModel.loadPrices() },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    uiState.errorMessage != null && uiState.prices.isEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CloudOff,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = uiState.errorMessage ?: "Something went wrong",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadPrices() }) {
                                Text("Retry")
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(uiState.prices.entries.toList()) { (coinId, priceInfo) ->
                                val isFavorited = uiState.favorites.any { it.coinId == coinId }
                                CoinRow(
                                    coinId = coinId,
                                    price = priceInfo.usd,
                                    direction = uiState.priceChanges[coinId] ?: PriceDirection.UNCHANGED,
                                    sparklineData = uiState.sparklineData[coinId] ?: emptyList(),
                                    isFavorited = isFavorited,
                                    onToggleFavorite = { viewModel.toggleFavorite(coinId, coinId) },
                                    onClick = { onCoinClick(coinId, priceInfo.usd) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun CoinRow(
    coinId: String,
    price: Double,
    direction: PriceDirection,
    sparklineData: List<Double>,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    val displayName = coinId
        .split("-")
        .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }

    val scale by animateFloatAsState(
        targetValue = if (isFavorited) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val trendColor = when (direction) {
        PriceDirection.UP -> Color(0xFF1D9E75)
        PriceDirection.DOWN -> Color(0xFFE24B4A)
        PriceDirection.UNCHANGED -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayName.take(1),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatPrice(price, "USD"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (direction != PriceDirection.UNCHANGED) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (direction == PriceDirection.UP)
                                    Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = trendColor
                            )
                        }
                    }
                }
            }

            Sparkline(
                data = sparklineData,
                modifier = Modifier.width(60.dp).height(28.dp),
                lineColor = trendColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorited) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = if (isFavorited) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
                )
            }
        }
    }
}

@Composable
fun Sparkline(
    data: List<Double>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    if (data.size < 2) {
        Box(modifier = modifier)
        return
    }

    Canvas(modifier = modifier) {
        val min = data.min()
        val max = data.max()
        val range = (max - min).takeIf { it > 0 } ?: 1.0

        val stepX = size.width / (data.size - 1)
        val points = data.mapIndexed { index, value ->
            val x = index * stepX
            val normalizedY = ((value - min) / range).toFloat()
            val y = size.height - (normalizedY * size.height)
            Offset(x, y)
        }

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
//@Composable
//fun CoinRow(
//    coinId: String,
//    price: Double,
//    isFavorited: Boolean,
//    onToggleFavorite: () -> Unit,
//    onClick: () -> Unit
//) {
//    val scale by animateFloatAsState(
//        targetValue = if (isFavorited) 1.2f else 1f,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessMedium
//        ),
//        finishedListener = { /* could trigger a settle-back if desired */ }
//    )
//
//    // ...existing displayName, Card, Row code...
//
//    IconButton(onClick = onToggleFavorite) {
//        Icon(
//            imageVector = if (isFavorited) Icons.Filled.Star else Icons.Outlined.StarBorder,
//            contentDescription = if (isFavorited) "Remove from favorites" else "Add to favorites",
//            tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
//        )
//    }
//}