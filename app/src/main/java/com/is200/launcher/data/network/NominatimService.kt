package com.is200.launcher.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimService {

    @GET("search")
    suspend fun searchLocation(

        @Query("q")
        query: String,

        @Query("format")
        format: String = "json",

        @Query("limit")
        limit: Int = 8

    ): List<NominatimLocation>
}