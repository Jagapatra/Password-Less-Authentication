package com.example.passwordlessauth.data

sealed class OtpValidationResult {
    object Success : OtpValidationResult()
    data class Failure(val remainingAttempts: Int) : OtpValidationResult()
    object AttemptsExhausted : OtpValidationResult()
    object Expired : OtpValidationResult()
}
