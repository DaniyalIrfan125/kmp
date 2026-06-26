package com.example.di

import com.example.database.getDatabaseBuilder
import com.example.database.getRoomDatabase
import org.koin.core.context.startKoin
import org.koin.dsl.module

val coreModule = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
}

fun initKoin() {
    startKoin {
        modules(coreModule)
    }
}