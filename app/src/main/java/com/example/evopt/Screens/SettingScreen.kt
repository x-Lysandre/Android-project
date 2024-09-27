package com.example.evopt.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.evopt.R

@Composable
fun Setting(){
    SettingsScreen()
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Profile Icon
        Icon(
            painter = painterResource(id = R.drawable.img_1), // Your Google icon
            contentDescription = "Profile Icon",
            modifier = Modifier
                .size(190.dp) // Adjust the size as needed
                .background(Color.White, shape = RoundedCornerShape(75.dp)) // Background color for visibility
                .padding(24.dp) // Padding to give some space within the icon
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Username
        Text(
            text = "Username",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )

        // Google Email
        Text(
            text = "user@gmail.com", // Replace with actual email
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Button
        Button(
            onClick = { /* Handle settings click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White // White background
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings, // Settings icon
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp),
                tint = Color.Black // Icon color
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(text = "Settings", color = Color.Black) // Text next to the icon
        }
    }
}

