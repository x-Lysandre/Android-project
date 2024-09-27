@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.evopt.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.evopt.R

@Composable
fun Charger() {
    var selectedOption by remember { mutableStateOf("") }
    val options = listOf("Option 1", "Option 2", "Option 3")

    Column(modifier = Modifier.padding(16.dp)) {
        // Dropdown Menu
        CustomSpinner2(
            options = options,
            label = "Select an Option",
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        SearchBar()

        Spacer(modifier = Modifier.height(16.dp))

        // QR Code Scanner Placeholder
        QRCodeScannerPlaceholder()
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text(text = "Search Charger") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xd4ebf2), shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xd4ebf2),
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun QRCodeScannerPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Height of the QR code scanner box
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Display the QR Code Scanner placeholder image
        Image(
            painter = painterResource(id = R.drawable.qrcode), // Replace with your drawable resource name
            contentDescription = "QR Code Scanner Placeholder",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSpinner2(
    options: List<String>,
    label: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xd4ebf2), shape = RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selectedOption.ifEmpty { "Select an option" },
                textAlign = TextAlign.Start,
                color = if (selectedOption.isEmpty()) Color.Gray else Color.Black
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, modifier = Modifier.padding(8.dp)) },
                    onClick = {
                        onOptionSelected(option) // Update selected option
                        expanded = false // Close the menu
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
