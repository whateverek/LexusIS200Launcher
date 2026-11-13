package com.is200.launcher.maps

data class RouteResponse(
    val paths: List<RoutePath>
)

data class RoutePath(
    val distance: Double,
    val time: Long,
    val points: RoutePoints
)

data class RoutePoints(
    val coordinates: List<List<Double>>
)