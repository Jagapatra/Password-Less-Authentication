package com.example.passwordlessauth.data

data class OtpData(
    val code: String,
    val expiryTimeMillis: Long,
    var remainingAttempts: Int
)
