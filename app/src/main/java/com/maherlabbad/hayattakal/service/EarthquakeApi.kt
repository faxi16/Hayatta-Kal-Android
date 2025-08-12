package com.maherlabbad.hayattakal.service

import com.maherlabbad.hayattakal.model.EarthquakeModel
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthquakeApi {
    @GET("apiv2/event/filter?orderby=timedesc&format=json")
    suspend fun getLatestEarthquakes(
        @Query("start") startDate: String,
        @Query("end") endDate: String
    ): List<EarthquakeModel>

}