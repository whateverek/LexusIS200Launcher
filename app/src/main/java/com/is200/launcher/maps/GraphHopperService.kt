package com.is200.launcher.maps

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GraphHopperService {

    @GET("route")
    suspend fun getRoute(

        @Query("point")
        points: List<String>,

        @Query("profile")
        profile: String = "car",

        @Query("locale")
        locale: String = "pl",

        @Query("calc_points")
        calcPoints: Boolean = true,

        @Query("points_encoded")
        pointsEncoded: Boolean = false,

        @Query("key")
        apiKey: String

    ): Response<RouteResponse>
}