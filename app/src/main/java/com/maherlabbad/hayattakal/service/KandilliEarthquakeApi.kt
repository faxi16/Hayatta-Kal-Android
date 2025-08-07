package com.maherlabbad.hayattakal.service

import com.maherlabbad.hayattakal.model.KandilliResponse
import retrofit2.http.GET

interface KandilliEarthquakeApi {
    @GET("live.php")
    suspend fun getLiveEarthquakes(): KandilliResponse
}