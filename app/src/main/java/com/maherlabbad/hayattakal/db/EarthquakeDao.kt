package com.maherlabbad.hayattakal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maherlabbad.hayattakal.model.EarthquakeModel

@Dao
interface EarthquakeDao {

    @Insert
    suspend fun insert(quake: EarthquakeModel)

    @Query("SELECT * FROM earthquakes WHERE  date = :date")
    suspend fun Contain(date : String) : EarthquakeModel?

    @Query("SELECT * FROM earthquakes ORDER BY date DESC")
    suspend fun getAll(): List<EarthquakeModel>

    @Query("DELETE FROM earthquakes WHERE date < :cutoff")
    suspend fun deleteOlderThan(cutoff : String)
}