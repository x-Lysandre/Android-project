package com.example.evopt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class LoginActivity : ComponentActivity() {
    private val validUsername = "Rohit"
    private val validPassword = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen { username, password ->
                signIn(username, password)
            }
        }
    }

    private fun signIn(username: String, password: String) {
        if (username == validUsername && password == validPassword) {
            navigateToMainActivity()
        } else {
            // Display an error message (you can use a Snackbar, Toast, or a Text)
            // For example, using a Toast
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        // Pass a flag to indicate successful login
        intent.putExtra("LOGIN_SUCCESS", true)
        startActivity(intent)
        finish() // Close LoginActivity
    }
}

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge, color = Color.Black)

        Spacer(modifier = Modifier.height(32.dp))

        // Username Input
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = { onLoginClick(username, password) },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Login")
        }
    }
}
