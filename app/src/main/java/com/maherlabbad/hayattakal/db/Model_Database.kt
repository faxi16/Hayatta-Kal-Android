package com.maherlabbad.hayattakal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maherlabbad.hayattakal.model.Relative_model

@Database(entities = [Relative_model::class], version = 1)
abstract class Model_Database: RoomDatabase() {
    abstract fun userDao() : Relative_Model_Dao

    companion object {
        @Volatile private var instance: Model_Database? = null

        fun getDatabase(context: Context): Model_Database {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    Model_Database::class.java,
                    "my_database"
                ).build().also { instance = it }
            }
        }
    }


}