@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.evopt.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun Depot() {
    var selectedOption by remember { mutableStateOf("") }
    val options = listOf("Option 1", "Option 2", "Option 3")

    // States for the count values
    var chargingCount by remember { mutableStateOf(10) }
    var availableCount by remember { mutableStateOf(15) }
    var faultedCount by remember { mutableStateOf(5) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Dropdown menu at the top
        CustomSpinner(
            options = options,
            label = "Select an Option",
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Three text fields in a row showing counts for Charging, Available, Faulted
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextFieldValue("Charging", chargingCount)
            TextFieldValue("Available", availableCount)
            TextFieldValue("Faulted", faultedCount)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab and pager screen for navigating between different screens
        TabScreen()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabScreen() {
    val pagerState = rememberPagerState()
    val tabTitles = listOf("Charging", "Available", "Faulted")
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Row with tabs for each screen
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = title, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                )
            }
        }

        // Pager to enable swipe gestures between tabs
        HorizontalPager(
            count = tabTitles.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ChargingScreen()
                1 -> AvailableScreen()
                2 -> FaultedScreen()
            }
        }
    }
}

@Composable
fun ChargingScreen() {
    // Content for Charging screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Charging Screen", fontSize = 24.sp)
    }
}

@Composable
fun AvailableScreen() {
    // Content for Available screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Available Screen", fontSize = 24.sp)
    }
}

@Composable
fun FaultedScreen() {
    // Content for Faulted screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEE)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Faulted Screen", fontSize = 24.sp)
    }
}

@Composable
fun TextFieldValue(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = label, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .background(Color(0xd4ebf2), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSpinner(
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
