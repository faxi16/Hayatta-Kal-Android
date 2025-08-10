package com.maherlabbad.hayattakal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earthquakes")
data class EarthquakeModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: String? = null,
    val date: String,
    val latitude: String,
    val longitude: String,
    val depth: String,
    val magnitude: String,
    val type: String,
    val location: String,
    var check : Boolean = false
)
