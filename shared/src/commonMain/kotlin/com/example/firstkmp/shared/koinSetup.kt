package com.example.firstkmp.shared

import com.example.firstkmp.shared.module.sharedModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModule)
    }
}