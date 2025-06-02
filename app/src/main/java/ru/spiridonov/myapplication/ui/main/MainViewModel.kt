package ru.spiridonov.myapplication.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail

class MainViewModel: ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    init {
        val token = VKID.Companion.instance.accessToken?.token
        Log.d("MainViewModel", "token: ${VKID.Companion.instance.accessToken?.token}")

        if (token != null) {
            _authState.value = AuthState.Authorized(token)
        } else {
            _authState.value = AuthState.NonAuthorized
        }
    }

    fun performAuthResult(state: AuthState) {
        _authState.value = state
    }

    fun setFail(fail: VKIDAuthFail) {
        when (fail) {
            is VKIDAuthFail.Canceled -> Log.d("LoginScreen", "Failed Canceled")
            is VKIDAuthFail.FailedApiCall -> Log.d("LoginScreen", "Failed ApiCall")
            is VKIDAuthFail.FailedOAuthState -> Log.d(
                "LoginScreen",
                "Failed OAuthState"
            )

            is VKIDAuthFail.FailedRedirectActivity -> Log.d(
                "LoginScreen",
                "Failed RedirectActivity"
            )

            is VKIDAuthFail.NoBrowserAvailable -> Log.d(
                "LoginScreen",
                "NoBrowserAvailable"
            )

            else -> Log.d("LoginScreen", "Another Error")
        }
    }
}
