package ru.spiridonov.myapplication.ui.main

sealed class AuthState {
    data class Authorized(val token: String) : AuthState()
    object Initial : AuthState()
    object NonAuthorized : AuthState()
}