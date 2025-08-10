package com.maherlabbad.hayattakal.repository

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.maps.model.LatLng
import com.maherlabbad.hayattakal.db.Relative_Model_Dao
import com.maherlabbad.hayattakal.location.getInstance
import kotlinx.coroutines.tasks.await


class RelativeRepository(private val relativeModelDao: Relative_Model_Dao,context : Context) {

    private val fusedLocation = getInstance(context)


    suspend fun getAllRelativeModels(): List<String> {
        return relativeModelDao.getAllPhones()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLocation() : LatLng? {
        val location = fusedLocation.lastLocation.await()
        return LatLng(location.latitude,location.longitude)
    }


}