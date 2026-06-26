package com.example.watchlist

import androidx.compose.ui.window.ComposeUIViewController
import com.example.feature.watchlist.initFeatureKoin

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        initFeatureKoin()
        koinStarted = true
    }
    App()
}


