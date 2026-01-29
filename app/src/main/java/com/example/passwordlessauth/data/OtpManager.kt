package com.example.passwordlessauth.data

import android.annotation.SuppressLint
import kotlin.random.Random

object OtpManager {

    private const val OTP_LENGTH = 6
    private const val OTP_EXPIRY_MILLIS = 60_000L
    private const val MAX_ATTEMPTS = 3

    // Stores OTP data per email
    private val otpStore = mutableMapOf<String, OtpData>()

    // Generate random 6-digit OTP
    @SuppressLint("DefaultLocale")
    private fun generateOtp(): String {
        val number = Random.nextInt(0, 1_000_000)
        return String.format("%06d", number)
    }

    // Create or resend OTP
    fun generateOtpForEmail(email: String): OtpData {
        val otp = generateOtp()
        val expiry = System.currentTimeMillis() + OTP_EXPIRY_MILLIS

        val otpData = OtpData(
            code = otp,
            expiryTimeMillis = expiry,
            remainingAttempts = MAX_ATTEMPTS
        )

        otpStore[email] = otpData
        return otpData
    }

    // Validate OTP entered by user
    fun validateOtp(email: String, enteredOtp: String): OtpValidationResult {

        val otpData = otpStore[email] ?: return OtpValidationResult.Expired

        // Expiry check
        if (System.currentTimeMillis() > otpData.expiryTimeMillis) {
            otpStore.remove(email)
            return OtpValidationResult.Expired
        }

        // Attempts exhausted
        if (otpData.remainingAttempts <= 0) {
            otpStore.remove(email)
            return OtpValidationResult.AttemptsExhausted
        }

        return if (otpData.code == enteredOtp) {
            otpStore.remove(email)
            OtpValidationResult.Success
        } else {
            otpData.remainingAttempts--

            if (otpData.remainingAttempts <= 0) {
                otpStore.remove(email)
                OtpValidationResult.AttemptsExhausted
            } else {
                OtpValidationResult.Failure(otpData.remainingAttempts)
            }
        }
    }

    // Get remaining time (for UI later)
    fun getRemainingTime(email: String): Long {
        val otpData = otpStore[email] ?: return 0L
        return otpData.expiryTimeMillis - System.currentTimeMillis()
    }
}
