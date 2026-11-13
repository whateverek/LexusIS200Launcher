
package com.is200.launcher.system.status

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationStatusMonitor(context: Context) {

    private val appContext = context.applicationContext

    private val locationManager =
        appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun observe(): Flow<LocationStatus> = callbackFlow {

        fun hasPermission(): Boolean {
            return ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }

        fun readStatus(): LocationStatus {
            if (!hasPermission()) {
                return LocationStatus.PERMISSION_REQUIRED
            }

            val enabled = try {
                locationManager.isLocationEnabled
            } catch (_: Exception) {
                false
            }

            if (!enabled) {
                return LocationStatus.DISABLED
            }

            val hasGpsProvider = try {
                locationManager.isProviderEnabled(
                    LocationManager.GPS_PROVIDER
                )
            } catch (_: Exception) {
                false
            }

            if (!hasGpsProvider) {
                return LocationStatus.NO_GPS_PROVIDER
            }

            return LocationStatus.SEARCHING
        }

        trySend(readStatus())

        val listener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                trySend(LocationStatus.FIXED)
            }

            override fun onProviderEnabled(provider: String) {
                trySend(readStatus())
            }

            override fun onProviderDisabled(provider: String) {
                trySend(readStatus())
            }

            @Deprecated("Deprecated in Android")
            override fun onStatusChanged(
                provider: String?,
                status: Int,
                extras: Bundle?
            ) {
                trySend(readStatus())
            }
        }

        val provider = LocationManager.GPS_PROVIDER

        if (readStatus() != LocationStatus.NO_GPS_PROVIDER &&
            readStatus() != LocationStatus.PERMISSION_REQUIRED &&
            readStatus() != LocationStatus.DISABLED
        ) {
            try {
                locationManager.requestLocationUpdates(
                    provider,
                    1000L,
                    1f,
                    listener,
                    Looper.getMainLooper()
                )
            } catch (_: SecurityException) {
                trySend(LocationStatus.PERMISSION_REQUIRED)
            } catch (_: IllegalArgumentException) {
                trySend(LocationStatus.NO_GPS_PROVIDER)
            }
        }

        awaitClose {
            try {
                locationManager.removeUpdates(listener)
            } catch (_: Exception) {
                // Monitor może zostać zamknięty po zmianie uprawnień.
            }
        }
    }
}
