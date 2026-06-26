package com.example.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object AppContextProvider {
    lateinit var context: Context
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val isInitialized = try {
        AppContextProvider.context
        true
    } catch (e: Exception) {
        false
    }
    println("DEBUG: getDatabaseBuilder() called, context initialized = $isInitialized")

    val appContext: Context = AppContextProvider.context
    val dbFile = appContext.getDatabasePath(DB_FILE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}