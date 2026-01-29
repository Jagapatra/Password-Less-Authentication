package com.example.passwordlessauth.viewmodel

sealed class AuthUiState {

    object Login : AuthUiState()

    data class OtpSent(
        val email: String,
        val expiryTimeMillis: Long,
        val errorMessage: String? = null
    ) : AuthUiState()

    data class LoggedIn(
        val email: String,
        val sessionStartTimeMillis: Long
    ) : AuthUiState()
}
