package ru.spiridonov.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ru.spiridonov.myapplication.ui.MainViewModel
import ru.spiridonov.myapplication.ui.theme.ApplicationTheme
import ru.spiridonov.myapplication.ui.views.MainScreen


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}