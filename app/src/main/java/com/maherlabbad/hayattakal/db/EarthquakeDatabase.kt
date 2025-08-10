package com.maherlabbad.hayattakal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maherlabbad.hayattakal.model.EarthquakeModel

@Database(entities = [EarthquakeModel::class], version = 1, exportSchema = false)
abstract class EarthquakeDatabase : RoomDatabase() {
    abstract fun earthquakeDao(): EarthquakeDao
    companion object {
        @Volatile private var instance: EarthquakeDatabase? = null

        fun getDatabase(context: Context): EarthquakeDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    EarthquakeDatabase::class.java,
                    "quakeDatabase"
                ).build().also { instance = it }
            }
        }
    }
}