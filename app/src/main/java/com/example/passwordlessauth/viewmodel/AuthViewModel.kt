package com.example.passwordlessauth.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordlessauth.data.OtpManager
import com.example.passwordlessauth.data.OtpValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber





class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Login)
    val uiState: StateFlow<AuthUiState> = _uiState
    private val _sessionDuration = MutableStateFlow("00:00")
    val sessionDuration = _sessionDuration.asStateFlow()

    private var sessionJob: Job? = null

    fun sendOtp(email: String) {
        viewModelScope.launch {
            val otpData = OtpManager.generateOtpForEmail(email)

            Timber.i("OTP generated for email=%s, otp=%s", email, otpData.code)

            _uiState.value = AuthUiState.OtpSent(
                email = email,
                expiryTimeMillis = otpData.expiryTimeMillis
            )

        }
    }


    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            when (val result = OtpManager.validateOtp(email, otp)) {

                is OtpValidationResult.Success -> {
                    Timber.i("OTP verification SUCCESS for email=%s", email)

                    val startTime = System.currentTimeMillis()
                    _uiState.value = AuthUiState.LoggedIn(
                        email = email,
                        sessionStartTimeMillis = startTime
                    )
                    startSessionTimer(startTime)
                }

                is OtpValidationResult.Failure -> {
                    Timber.w(
                        "OTP FAILED for email=%s, remainingAttempts=%d",
                        email,
                        result.remainingAttempts
                    )

                    val expiryTimeMillis =
                        System.currentTimeMillis() + OtpManager.getRemainingTime(email)

                    _uiState.value = AuthUiState.OtpSent(
                        email = email,
                        expiryTimeMillis = expiryTimeMillis,
                        errorMessage = "Invalid OTP. Attempts remaining: ${result.remainingAttempts}"
                    )
                }


                is OtpValidationResult.AttemptsExhausted -> {
                    Timber.w("OTP attempts exhausted for email=%s", email)

                    _uiState.value = AuthUiState.Login
                }

                is OtpValidationResult.Expired -> {
                    Timber.w("OTP expired for email=%s", email)

                    _uiState.value = AuthUiState.Login
                }
            }
        }
    }




    fun logout() {
        Timber.i("User logged out")

        sessionJob?.cancel()
        sessionJob = null
        _sessionDuration.value = "00:00"
        _uiState.value = AuthUiState.Login
    }



    @SuppressLint("DefaultLocale")
    private fun startSessionTimer(startTimeMillis: Long) {
        sessionJob?.cancel()

        sessionJob = viewModelScope.launch {
            while (true) {
                val elapsedMillis = System.currentTimeMillis() - startTimeMillis
                val minutes = (elapsedMillis / 1000) / 60
                val seconds = (elapsedMillis / 1000) % 60

                _sessionDuration.value =
                    String.format("%02d:%02d", minutes, seconds)

                delay(1000)
            }
        }
    }

}
