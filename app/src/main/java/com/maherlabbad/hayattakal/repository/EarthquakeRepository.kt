package com.maherlabbad.hayattakal.repository

import com.maherlabbad.hayattakal.Screens.getStartAndEndDatesForLast24Hours
import com.maherlabbad.hayattakal.db.EarthquakeDao
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.service.EarthquakeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EarthquakeRepository(private val dao: EarthquakeDao) {

    private val BASE_URL = "https://deprem.afad.gov.tr/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EarthquakeApi::class.java)
    suspend fun insertEarthquake(quake: EarthquakeModel): Boolean {
        if(dao.Contain(quake.date) == null) {
            dao.insert(quake)
            return true
        }
        return false
    }

    suspend fun deleteOldData(cutoff: String) {
        dao.deleteOlderThan(cutoff)
    }

    suspend fun getAllEarthquakes(): List<EarthquakeModel> {
        val (startDate, endDate) = getStartAndEndDatesForLast24Hours()
        return retrofit.getLatestEarthquakes(startDate, endDate)
    }

}