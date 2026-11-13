package com.is200.launcher.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val client =

        OkHttpClient.Builder()
            .build()

    private val retrofit =

        Retrofit.Builder()

            .baseUrl(
                "https://nominatim.openstreetmap.org/"
            )

            .client(client)

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

    val nominatimService =

        retrofit.create(
            NominatimService::class.java
        )
}