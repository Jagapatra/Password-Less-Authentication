package com.example.passwordlessauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.passwordlessauth.pages.LoginScreen
import com.example.passwordlessauth.pages.OtpScreen
import com.example.passwordlessauth.pages.SessionScreen
import com.example.passwordlessauth.ui.theme.PasswordlessAuthTheme
import com.example.passwordlessauth.viewmodel.AuthUiState
import com.example.passwordlessauth.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PasswordlessAuthTheme {

                val uiState by authViewModel.uiState.collectAsState()

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (uiState) {

                        is AuthUiState.Login -> {
                            LoginScreen(
                                onSendOtp = { email ->
                                    authViewModel.sendOtp(email)
                                }
                            )
                        }

                        is AuthUiState.OtpSent -> {
                            val state = uiState as AuthUiState.OtpSent

                            OtpScreen(
                                email = state.email,
                                expiryTimeMillis = state.expiryTimeMillis,
                                errorMessage = state.errorMessage,
                                onVerifyOtp = { otp ->
                                    authViewModel.verifyOtp(state.email, otp)
                                },
                                onResendOtp = {
                                    authViewModel.sendOtp(state.email)
                                }
                            )
                        }


                        is AuthUiState.LoggedIn -> {
                            val sessionDuration by authViewModel.sessionDuration.collectAsState()
                            val state = uiState as AuthUiState.LoggedIn

                            SessionScreen(
                                email = state.email,
                                sessionDuration = sessionDuration,
                                onLogout = {
                                    authViewModel.logout()
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}
