package com.example.firstkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.di.initKoin

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }
    App()
}


