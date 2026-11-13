
package com.is200.launcher.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.is200.launcher.R

private fun hasLocationPermission(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarse = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fine || coarse
}

@SuppressLint("MissingPermission")
@Composable
fun rememberCarLocation(): Location? {

    val context = LocalContext.current

    var location by remember {
        mutableStateOf<Location?>(null)
    }

    val permissionGranted = remember(context) {
        hasLocationPermission(context)
    }

    DisposableEffect(context, permissionGranted) {

        if (!permissionGranted) {
            onDispose {}
        } else {

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000L
            ).build()

            val callback = object : LocationCallback() {

                override fun onLocationResult(
                    result: LocationResult
                ) {
                    location = result.lastLocation
                }
            }

            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            location = result
                        }
                    }

                fusedLocationClient.requestLocationUpdates(
                    request,
                    callback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                location = null
            }

            onDispose {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
    }

    return location
}

@SuppressLint("MissingPermission")
@Composable
fun RealMapView() {

    val context = LocalContext.current

    val currentLocation = rememberCarLocation()

    val locationPermissionGranted = remember(context) {
        hasLocationPermission(context)
    }

    val defaultLocation = LatLng(
        51.7592,
        19.4550
    )

    val targetLocation =
        NavigationManager.selectedLocation.value
            ?: currentLocation?.let {
                LatLng(
                    it.latitude,
                    it.longitude
                )
            }
            ?: defaultLocation

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            targetLocation,
            16f
        )
    }

    LaunchedEffect(targetLocation) {
        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(
                targetLocation,
                16f
            )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),

        cameraPositionState = cameraPositionState,

        properties = MapProperties(
            isMyLocationEnabled = locationPermissionGranted
        ),

        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false
        )
    ) {

        MapEffect(Unit) { map ->
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )
        }
    }
}
