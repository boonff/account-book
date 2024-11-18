package me.accountbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.accountbook.koin.androidModule
import me.accountbook.ui.navigation.AndroidNav
import me.accountbook.ui.theme.AndroidTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 Koin
        startKoin {
            androidContext(this@MainActivity)
            modules(androidModule)
        }

        setContent {
            AppAndroidContent()
        }
    }
}

@Composable
fun AppAndroidContent() {
    AndroidTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        )
        AndroidNav(rememberNavController())

    }
}