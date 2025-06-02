package ru.spiridonov.myapplication.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.id.VKID
import ru.spiridonov.myapplication.ui.theme.ApplicationTheme
import ru.spiridonov.myapplication.ui.views.MainScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.init(this)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val authState = viewModel.authState.observeAsState(AuthState.Initial)

            ApplicationTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    when (authState.value) {
                        is AuthState.Authorized -> MainScreen()

                        is AuthState.NonAuthorized -> LoginScreen(viewModel)
                        AuthState.Initial -> {}
                    }
                }
            }
        }
    }
}