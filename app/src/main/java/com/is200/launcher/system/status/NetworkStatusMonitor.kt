
package com.is200.launcher.system.status

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkStatusMonitor(context: Context) {

    private val connectivityManager =
        context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

    fun observe(): Flow<NetworkStatus> = callbackFlow {

        fun readStatus(): NetworkStatus {
            val network = connectivityManager.activeNetwork
                ?: return NetworkStatus.DISCONNECTED

            val capabilities =
                connectivityManager.getNetworkCapabilities(network)
                    ?: return NetworkStatus.DISCONNECTED

            val hasInternetCapability =
                capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )

            val hasValidatedInternet =
                capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )

            if (!hasInternetCapability || !hasValidatedInternet) {
                return NetworkStatus.CONNECTED_NO_INTERNET
            }

            return when {
                capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) -> NetworkStatus.WIFI

                capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) -> NetworkStatus.MOBILE

                capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_ETHERNET
                ) -> NetworkStatus.ETHERNET

                else -> NetworkStatus.OTHER
            }
        }

        trySend(readStatus())

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                trySend(readStatus())
            }

            override fun onLost(network: Network) {
                trySend(readStatus())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(readStatus())
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
