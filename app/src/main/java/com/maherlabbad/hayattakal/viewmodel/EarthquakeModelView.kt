package com.maherlabbad.hayattakal.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.viewModelScope
import com.maherlabbad.hayattakal.getStartAndEndDatesForLast24Hours
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.model.KandilliEarthquake
import com.maherlabbad.hayattakal.service.EarthquakeApi
import com.maherlabbad.hayattakal.service.KandilliEarthquakeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EarthquakeViewModel(application: Application) : AndroidViewModel(application) {
    private val BASE_URL = "https://deprem.afad.gov.tr/"
    private val BASE_URL_KANDILLI = "https://api.kandilli.org/"

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

    val Earthquakes = mutableStateOf<List<EarthquakeModel>>(listOf())
    val EarthquakesKandilli = mutableStateOf<List<KandilliEarthquake>>(listOf())


    init {
        loadDataAfad()
        loadDataKandilli()
    }

    fun loadDataAfad(){
        viewModelScope.launch(Dispatchers.IO){
            try {

                val Date = getStartAndEndDatesForLast24Hours()
                val startDate = Date.first
                val endDate = Date.second
                val response = retrofit.getLatestEarthquakes(startDate, endDate)
                Earthquakes.value = response

            }catch (e : Exception){
                Log.e("EarthquakeViewModel", "Error loading data", e)
            }

        }
    }

    fun loadDataKandilli(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = retrofitKandilli.getLiveEarthquakes()
                EarthquakesKandilli.value = response.result
            }catch (e : Exception){
                Log.e("EarthquakeViewModel", "Error loading data", e)
            }

        }
    }



}