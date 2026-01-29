package com.example.passwordlessauth.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun SessionScreen(
    email: String,
    sessionDuration: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome $email")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Session Duration: $sessionDuration")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}
