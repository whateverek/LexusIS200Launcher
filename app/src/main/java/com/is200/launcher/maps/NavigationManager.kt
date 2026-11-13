package com.is200.launcher.maps

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import com.is200.launcher.data.network.NetworkModule
import com.is200.launcher.data.network.NominatimLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object NavigationManager {

    private fun distanceScore(

        lat1: Double,
        lon1: Double,

        lat2: Double,
        lon2: Double

    ): Double {

        val dLat =
            Math.toRadians(lat2 - lat1)

        val dLon =
            Math.toRadians(lon2 - lon1)

        val a =

            sin(dLat / 2).pow(2) +

                    cos(Math.toRadians(lat1)) *
                    cos(Math.toRadians(lat2)) *

                    sin(dLon / 2).pow(2)

        return 6371 * 2 * atan2(
            sqrt(a),
            sqrt(1 - a)
        )
    }

    val searchResults =
        mutableStateListOf<NominatimLocation>()

    val selectedLocation =
        mutableStateOf<LatLng?>(null)

    private var searchJob: Job? = null

    private val searchCache =

        mutableMapOf<
                String,
                List<NominatimLocation>
                >()

    fun searchLocation(
        query: String
    ) {

        if (query.trim().length < 3) {

            searchResults.clear()

            return
        }

        val normalizedQuery =

            query
                .trim()
                .lowercase()

        searchCache[normalizedQuery]
            ?.let { cached ->

                searchResults.clear()

                searchResults.addAll(cached)

                return
            }

        searchJob?.cancel()

        searchJob =

            CoroutineScope(Dispatchers.IO)
                .launch {

                    delay(350)

                    try {

                        val rawResults =

                            NetworkModule
                                .nominatimService
                                .searchLocation(query)

                        val sortedResults =

                            rawResults.sortedBy { location ->

                                try {

                                    val lat =
                                        location.lat.toDouble()

                                    val lon =
                                        location.lon.toDouble()

                                    distanceScore(

                                        51.7592,
                                        19.4550,

                                        lat,
                                        lon
                                    )

                                } catch (_: Exception) {

                                    Double.MAX_VALUE
                                }
                            }

                        searchResults.clear()

                        searchResults.addAll(
                            sortedResults
                        )

                        searchCache[normalizedQuery] =
                            sortedResults

                    } catch (_: Exception) {

                    }
                }
    }

    fun selectLocation(
        location: NominatimLocation
    ) {

        selectedLocation.value =

            LatLng(
                location.lat.toDouble(),
                location.lon.toDouble()
            )

        searchResults.clear()
    }
}