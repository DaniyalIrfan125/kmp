package com.example.firstkmp.shared.module


import com.example.firstkmp.shared.getDatabaseBuilder
import com.example.firstkmp.shared.getRoomDatabase
import com.example.firstkmp.shared.repository.WatchlistRepository
import com.example.firstkmp.shared.viewmodels.WatchlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module



val sharedModule = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
    single { WatchlistRepository(database = get()) }
    viewModel { WatchlistViewModel(repository = get()) }
}