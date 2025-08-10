package com.maherlabbad.hayattakal

import android.content.Context

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object location {
    private var FusedLocationProviderClient : FusedLocationProviderClient? = null


    fun getInstance(context : Context) : FusedLocationProviderClient {
        if(FusedLocationProviderClient == null){
            FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        }
        return FusedLocationProviderClient!!
    }
}