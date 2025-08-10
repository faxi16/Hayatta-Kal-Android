package com.maherlabbad.hayattakal.viewmodel

import android.Manifest
import android.app.Application
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.Relative_model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RelativeModelviewmodel(application: Application) : AndroidViewModel(application) {
    private val db = Model_Database.getDatabase(getApplication())

    private val ModelDao = db.userDao();

    val itemList = mutableStateOf<List<Relative_model>>(listOf())

    val latlng = mutableStateOf(LatLng(0.0,0.0))

    fun getItemList(){
        viewModelScope.launch(Dispatchers.IO) {
            itemList.value = ModelDao.getItemWithNameAndId()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationUpdates(
        fusedLocationClient: FusedLocationProviderClient
    ) {
        viewModelScope.launch {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latlng.value = LatLng(location.latitude, location.longitude)
                }
            }
        }
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            60000
        ).setMinUpdateDistanceMeters(50f)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    latlng.value = LatLng(location.latitude, location.longitude)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    fun saveItem(item: Relative_model){
        viewModelScope.launch(Dispatchers.IO) {
            val relative = ModelDao.Contains(item.phone_number)
            if(relative == null){
                ModelDao.insert(item)
                getItemList()
            }else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Bu kişi zaten kayıtlı", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun deleteItem(item : Relative_model){
        viewModelScope.launch(Dispatchers.IO) {
            ModelDao.Delete(item)
            getItemList()
        }
    }
}