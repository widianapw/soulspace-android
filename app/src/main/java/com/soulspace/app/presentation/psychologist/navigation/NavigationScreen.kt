package com.soulspace.app.presentation.psychologist.navigation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.soulspace.app.R
import com.utsman.osmandcompose.MapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.Polyline
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import kotlinx.serialization.Serializable
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

@Serializable
data class NavigationRoute(
    val toLatitude: String,
    val toLongitude: String
)

@Composable
fun NavigationScreen(
    navController: NavController,
    args: NavigationRoute,
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val toLatitude = args.toLatitude.toDouble()
    val toLongitude = args.toLongitude.toDouble()
    val context = LocalContext.current

// State to hold user's current location as GeoPoint
    var userLocation by remember {
        mutableStateOf<GeoPoint?>(null)
    }
    // State to track if the route has already been fetched
    var hasFetchedRoute by remember { mutableStateOf(false) }


    val heading by rememberCompassHeading(context)// Log the heading to ensure it is being updated
    // Load the baseline_navigation_24 drawable resource directly
    val baseMarkerIcon =
        remember { getDrawableFromResource(context, R.drawable.baseline_navigation_24) }


    // Create MarkerState and update the position
    val markerState = userLocation?.let { rememberMarkerState(geoPoint = it, rotation = heading) }
    val locationMarkerState = rememberMarkerState(geoPoint = GeoPoint(toLatitude, toLongitude))

    // Update the marker rotation when the heading changes
    LaunchedEffect(heading) {
        markerState?.rotation = heading
    }

    // Camera state initialized after the first location is retrieved
    val cameraState = rememberCameraState {
        geoPoint = userLocation ?: GeoPoint(-7.80141254430627, 110.3647667822073)
        zoom = 17.0
    }

    // Listen for location changes and update the marker and camera
    GetCurrentLocation(context) { location ->
        val currentGeoPoint = GeoPoint(location.latitude, location.longitude)

        // Update marker position and camera position when the location changes
        userLocation = currentGeoPoint
        markerState?.geoPoint = currentGeoPoint
        cameraState.geoPoint = currentGeoPoint
        cameraState.zoom = 18.0
    }

// Observe the state from ViewModel
    val navigationState by viewModel.state
    // Handle loading, error, and success cases
    // Define polyline dynamically based on the fetched route
    val polyline = navigationState.list.map { item ->
        GeoPoint(item.lat, item.lng)
    }

    // Fetch the route using ViewModel
    LaunchedEffect(key1 = userLocation) {
        if (userLocation != null && !hasFetchedRoute) {
            userLocation?.let { location ->
                viewModel.getRoute(
                    fromLatitude = location.latitude,
                    fromLongitude = location.longitude,
                    toLatitude = toLatitude,
                    toLongitude = toLongitude
                )
            }
            hasFetchedRoute = true // Mark the route as fetched
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column() {
                OpenStreetMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) { detectTapGestures { } },
                    cameraState = cameraState,
                    properties = MapProperties(
                        tileSources = TileSourceFactory.DEFAULT_TILE_SOURCE
                    )
                ) {
                    if (polyline.isNotEmpty()) {
                        Polyline(polyline, color = Color.Green)
                        Marker(state = locationMarkerState)
                        if (markerState != null) {
                            Marker(markerState, icon = baseMarkerIcon)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }

            Button(
                onClick = {
                    val gmmIntentUri =
                        Uri.parse("google.navigation:q=${toLatitude},${toLongitude}")
                    val mapIntent =
                        Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
            ) {
                Text(text = "Google Maps")
            }

            // Floating Back Button (top left)
//            Button(
//                icon = Icons.Default.ArrowBack,
//                description = "Back",
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .padding(16.dp)
//            ) {
//                navController.popBackStack() // Navigates back
//            }

        }
    }
}

// Helper function to load the Drawable from resources
fun getDrawableFromResource(context: Context, resourceId: Int): Drawable? {
    return ResourcesCompat.getDrawable(context.resources, resourceId, context.theme)
}

// Helper function to convert Drawable to Bitmap and rotate it based on the heading
fun rotateDrawable(context: Context, drawable: Drawable, angle: Float): Drawable {
    val bitmap = when (drawable) {
        is BitmapDrawable -> drawable.bitmap
        is VectorDrawable -> drawable.toBitmap() // Convert VectorDrawable to Bitmap
        else -> throw IllegalArgumentException("Unsupported drawable type")
    }

    val matrix = Matrix()
    matrix.postRotate(angle)

    // Create a new rotated bitmap
    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    // Return the rotated Drawable
    return BitmapDrawable(context.resources, rotatedBitmap)
}