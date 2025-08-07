package com.maherlabbad.hayattakal.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maherlabbad.hayattakal.Screens.getStartAndEndDatesForLast24Hours
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.service.EarthquakeApi
import com.maherlabbad.hayattakal.service.KandilliEarthquakeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EarthquakeViewModel(application: Application) : AndroidViewModel(application) {
    private val BASE_URL = "https://deprem.afad.gov.tr/"
    private val BASE_URL_KANDILLI = "https://api.orhanaydogdu.com.tr/"

    private val retrofitKandilli =
        Retrofit.Builder()
            .baseUrl(BASE_URL_KANDILLI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KandilliEarthquakeApi::class.java)

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EarthquakeApi::class.java)

    private val _dataSource = MutableStateFlow("AFAD")
    val dataSource = _dataSource.asStateFlow()


    private val _earthquakes = MutableStateFlow<List<EarthquakeModel>>(emptyList())
    val earthquakes = _earthquakes.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchData()
    }

    fun toggleDataSource() {
        _dataSource.value = if (_dataSource.value == "AFAD") "Kandilli" else "AFAD"
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val result = if (_dataSource.value == "AFAD") {
                    fetchAfadData()
                } else {
                    fetchKandilliData()
                }
                _earthquakes.value = result
            } catch (e: Exception) {

                e.printStackTrace()
                Log.e("EarthquakeViewModel", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchAfadData(): List<EarthquakeModel> {
        val (startDate, endDate) = getStartAndEndDatesForLast24Hours()
        return retrofit.getLatestEarthquakes(startDate, endDate)
    }

    private suspend fun fetchKandilliData(): List<EarthquakeModel> {
        val response = retrofitKandilli.getLiveEarthquakes()

        return response.result.map { kandilli ->
            EarthquakeModel(
                eventId = kandilli.earthquake_id,
                date = kandilli.date,
                latitude = kandilli.geojson.coordinates.getOrElse(1) { 0.0 }.toString(),
                longitude = kandilli.geojson.coordinates.getOrElse(0) { 0.0 }.toString(),
                depth = kandilli.depth.toString(),
                magnitude = kandilli.mag.toString(),
                type = "ML",
                location = kandilli.title
            )
        }
    }

}