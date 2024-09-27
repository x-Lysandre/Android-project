@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.example.evopt.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi

private const val TAG = "Maps"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Maps() {
    val context = LocalContext.current

    // Location permissions state
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Initialize FusedLocationProviderClient for getting current location
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedMarkerInfo by remember { mutableStateOf<String?>(null) }

    // Check and request permissions
    LaunchedEffect(locationPermissionsState) {
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        } else {
            if (!isLocationEnabled(context)) {
                Toast.makeText(context, "Please enable location services", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } else {
                Log.d(TAG, "Location permissions granted and services enabled.")
                // Get the current location if permissions are granted and location services are enabled
                getCurrentLocation(fusedLocationClient) { location ->
                    if (location != null) {
                        currentLocation = GeoPoint(location.latitude, location.longitude)
                        Log.d(TAG, "Current location retrieved: ${currentLocation}")
                    } else {
                        Log.e(TAG, "Failed to retrieve current location.")
                        Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Generate markers from fixed points
    val fixedPoints = listOf(
        Pair(GeoPoint(28.6139, 77.2090), "Connaught Place"),
        Pair(GeoPoint(28.7041, 77.1025), "India Gate"),
        Pair(GeoPoint(28.5504, 77.1688), "Qutub Minar"),
        Pair(GeoPoint(28.4810, 77.0967), "Akshardham Temple"),
        Pair(GeoPoint(28.6132, 77.2095), "Lotus Temple"),
        Pair(GeoPoint(28.6129, 77.2295), "Hauz Khas Village"),
        Pair(GeoPoint(28.5645, 77.2177), "Red Fort")
    )

    // Display the map with the current location set as the center
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapViewContext ->
                MapView(mapViewContext).apply {
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)

                    // Add MyLocationNewOverlay to display the user's location on the map
                    val myLocationOverlay = MyLocationNewOverlay(this).apply {
                        enableMyLocation()
                    }
                    overlays.add(myLocationOverlay)

                    // Create markers from fixed points
                    fixedPoints.forEach { (point, title) ->
                        Marker(this).apply {
                            position = point
                            this.title = title
                            setOnMarkerClickListener { _, _ ->
                                selectedMarkerInfo = title
                                showBottomSheet = true
                                true
                            }
                        }.let { overlays.add(it) }
                    }
                }
            },
            update = { mapView ->
                currentLocation?.let {
                    mapView.controller.setCenter(it)
                }
            }
        )
    }

    // Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            BottomSheetContent(markerInfo = selectedMarkerInfo)
        }
    }
}

@Composable
fun BottomSheetContent(markerInfo: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Marker Info", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = markerInfo ?: "No information available")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Handle some action here */ }) {
            Text("Do Something")
        }
    }
}

/**
 * Retrieves the user's current location using the FusedLocationProviderClient.
 *
 * @param fusedLocationClient The FusedLocationProviderClient instance.
 * @param onLocationReceived A callback to handle the retrieved location.
 */
@SuppressLint("MissingPermission") // Be cautious with this annotation
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location?) -> Unit
) {
    val locationTask: Task<Location> = fusedLocationClient.lastLocation
    locationTask.addOnSuccessListener { location: Location? ->
        onLocationReceived(location)
    }.addOnFailureListener { e ->
        Log.e(TAG, "Error retrieving location: ${e.message}")
        onLocationReceived(null)
    }
}

/**
 * Checks if the location services are enabled on the device.
 *
 * @param context The context to access system services.
 * @return true if location is enabled, false otherwise.
 */
fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}
