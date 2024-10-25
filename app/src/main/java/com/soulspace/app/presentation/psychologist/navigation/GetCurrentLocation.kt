package com.soulspace.app.presentation.psychologist.navigation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

// Helper function to compare two locations (you can also use the distance method to check proximity)
fun hasPositionChanged(previous: Location?, current: Location): Boolean {
    return previous?.latitude != current.latitude || previous?.longitude != current.longitude
}

@Composable
fun GetCurrentLocation(context: Context, onLocationChanged: (Location) -> Unit) {
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = remember {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // Location update interval in milliseconds
        ).build()
    }

    var previousLocation by remember { mutableStateOf<Location?>(null) }

    DisposableEffect(Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null && hasPositionChanged(previousLocation, location)) {
                    previousLocation = location
                    onLocationChanged(location) // Notify that the user's position has changed
                }
            }
        }

        // Start receiving location updates
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}