package com.maherlabbad.hayattakal.model

data class EarthquakeModel(
    val eventId: String,
    val date: String,
    val latitude: String,
    val longitude: String,
    val depth: String,
    val magnitude: String,
    val type: String,
    val location: String
)
