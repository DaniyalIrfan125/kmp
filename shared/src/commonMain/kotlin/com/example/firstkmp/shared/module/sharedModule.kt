package com.example.firstkmp.shared.module


import com.example.firstkmp.shared.repository.WatchlistRepository
import com.example.firstkmp.shared.viewmodels.WatchlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val watchlistFeatureModule = module {
    single { WatchlistRepository(database = get()) }
    viewModel { WatchlistViewModel(repository = get()) }
}