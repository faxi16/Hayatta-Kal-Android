package com.maherlabbad.hayattakal.service

import com.maherlabbad.hayattakal.model.KandilliResponse
import retrofit2.http.GET

interface KandilliEarthquakeApi {
    @GET("deprem/kandilli/live")
    suspend fun getLiveEarthquakes(): KandilliResponse
}