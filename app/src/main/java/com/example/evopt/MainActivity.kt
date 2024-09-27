package com.example.evopt

import com.example.evopt.Screens.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evopt.ui.theme.EVoptTheme
import com.google.firebase.auth.FirebaseAuth
import org.osmdroid.config.Configuration

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private const val PREF_NAME = "UserPreferences"
private const val KEY_UID = "uid"
private const val KEY_EMAIL = "email"
private const val KEY_LOGGED_IN = "isLoggedIn"

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Configuration.getInstance().userAgentValue = packageName

            EVoptTheme {
                val loginSuccess = intent.getBooleanExtra("LOGIN_SUCCESS", false)
                val userPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val isLoggedIn = userPreferences.getBoolean(KEY_LOGGED_IN, false)

                if (!isLoggedIn && !loginSuccess) {
                    // If user is not logged in and no login success flag, redirect to LoginActivity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish() // Close MainActivity
                } else {

                    if (loginSuccess) {
                        storeCurrentUserLocally(
                            context = this,
                            uid = "local_user_uid", // Replace with actual user info if available
                            email = "local_user_email@example.com"
                        )
                    }

                    var expanded by remember { mutableStateOf(false) }

                    val items = listOf(
                        BottomNavigationItem(
                            title = "Map",
                            selectedIcon = Icons.Filled.LocationOn,
                            unselectedIcon = Icons.Outlined.LocationOn
                        ),
                        BottomNavigationItem(
                            title = "Depot",
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home
                        ),
                        BottomNavigationItem(
                            title = "Charger",
                            selectedIcon = Icons.Filled.Search,
                            unselectedIcon = Icons.Outlined.Search
                        ),
                        BottomNavigationItem(
                            title = "Setting",
                            selectedIcon = Icons.Filled.Settings,
                            unselectedIcon = Icons.Outlined.Settings
                        )
                    )

                    var selectedItemIndex by remember { mutableStateOf(0) }
                    val navController = rememberNavController()

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = "EV opts") },
                                actions = {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "More options"
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(onClick = {
                                            expanded = false // Close the menu
                                            onLogout(this@MainActivity) // Trigger the logout function
                                        }) {
                                            Text("Logout")
                                        }
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            when (index) {
                                                0 -> navController.navigate("map")
                                                1 -> navController.navigate("Depot")
                                                2 -> navController.navigate("Charger")
                                                3 -> navController.navigate("Setting")
                                            }
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else {
                                                    item.unselectedIcon
                                                },
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(navController = navController, startDestination = "map", Modifier.padding(innerPadding)) {
                            composable("map") { Maps() }
                            composable("Depot") { Depot() }
                            composable("Charger") { Charger() }
                            composable("Setting") { Setting() }
                        }
                        BackHandler {
                            finish()
                        }
                    }
                }
            }
        }
    }
}

// Store current user information locally
fun storeCurrentUserLocally(context: Context, uid: String?, email: String?) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(KEY_UID, uid)
    editor.putString(KEY_EMAIL, email)
    editor.putBoolean(KEY_LOGGED_IN, true) // Set logged-in status
    editor.apply() // Apply changes asynchronously}
}

// Function to retrieve stored user info
fun retrieveStoredUser(context: Context): Map<String, String?> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return mapOf(
        "uid" to sharedPreferences.getString(KEY_UID, null),
        "email" to sharedPreferences.getString(KEY_EMAIL, null)
    )
}

@Composable
fun EVoptTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFADD8E6),  // Light blue
            onPrimary = Color.White,
            secondary = Color(0xFF87CEEB), // Sky blue
            onSecondary = Color.White,
            background = Color.White,
            surface = Color(0xFFE3F2FD),
            onSurface = Color.Black,
            error = Color.Red,
            onError = Color.White,
        ),
        content = content
    )
}

fun onLogout(context: Context) {
    // Handle logout logic
    FirebaseAuth.getInstance().signOut() // Sign out of Firebase, if applicable

    // Start LoginActivity
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the back stack
    context.startActivity(intent) // Start LoginActivity
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EVoptTheme {
        // Preview your UI here
        Text(text = "Preview of EV opts")
    }
}
