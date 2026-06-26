package com.example.firstkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.firstkmp.shared.initKoin
import com.example.firstkmp.shared.module.sharedModule
import org.koin.core.context.startKoin
private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }
    App()
}


