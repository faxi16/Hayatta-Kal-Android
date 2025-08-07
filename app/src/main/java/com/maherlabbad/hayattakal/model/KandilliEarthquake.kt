package com.maherlabbad.hayattakal.model


data class KandilliResponse(
    val status: Boolean,
    val result: List<KandilliEarthquake>
)

data class KandilliEarthquake(
    val earthquake_id: String,
    val title: String,
    val date: String,
    val mag: Double,
    val depth: Double,
    val geojson: GeoJson
)

data class GeoJson(
    val type: String,
    val coordinates: List<Double>
)
