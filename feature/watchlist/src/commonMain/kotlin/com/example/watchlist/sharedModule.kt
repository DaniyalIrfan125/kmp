package com.example.feature.watchlist

import com.example.di.coreModule
import com.example.watchlist.WatchlistRepository
import com.example.watchlist.WatchlistViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val watchlistFeatureModule = module {
    single { WatchlistRepository(database = get()) }
    viewModel { WatchlistViewModel(repository = get()) }
}

fun initFeatureKoin() {
    startKoin {
        modules(coreModule, watchlistFeatureModule)
    }
}