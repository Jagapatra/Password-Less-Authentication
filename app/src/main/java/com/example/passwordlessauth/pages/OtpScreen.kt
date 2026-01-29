package com.example.passwordlessauth.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    email: String,
    expiryTimeMillis: Long,
    errorMessage: String?,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
)
 {

    var otp by remember { mutableStateOf("") }
    if (errorMessage != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var remainingSeconds by remember { mutableStateOf(0L) }

        LaunchedEffect(expiryTimeMillis) {
            while (true) {
                val remaining =
                    (expiryTimeMillis - System.currentTimeMillis()) / 1000

                if (remaining <= 0) {
                    remainingSeconds = 0
                    break
                }

                remainingSeconds = remaining
                delay(1000)
            }
        }


        Text("OTP sent to $email")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Expires in ${remainingSeconds} seconds")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { if (it.length <= 6) otp = it },
            label = { Text("Enter OTP") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onVerifyOtp(otp) },
            enabled = otp.length == 6
        ) {
            Text("Verify OTP")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onResendOtp) {
            Text("Resend OTP")
        }
    }
}
