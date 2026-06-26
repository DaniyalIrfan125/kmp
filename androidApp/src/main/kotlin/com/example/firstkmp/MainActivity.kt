package com.example.firstkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.database.AppContextProvider
import com.example.di.initKoin
import com.example.firstkmp.shared.module.watchlistFeatureModule
import org.koin.core.context.loadKoinModules


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        AppContextProvider.context = applicationContext
        initKoin()

        loadKoinModules(watchlistFeatureModule)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}