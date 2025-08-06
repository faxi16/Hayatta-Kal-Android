package com.maherlabbad.hayattakal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maherlabbad.hayattakal.model.Relative_model

@Database(entities = [Relative_model::class], version = 1)
abstract class Model_Database: RoomDatabase() {
    abstract fun userDao() : Relative_Model_Dao
}