package com.maherlabbad.hayattakal.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.maherlabbad.hayattakal.model.Relative_model

@Dao
interface Relative_Model_Dao {

    @Query("SELECT * FROM Relative_model")
    fun getItemWithNameAndId() : List<Relative_model>

    @Query("SELECT * FROM Relative_model WHERE Phone_number = :phoneNumber")
    fun Contains(phoneNumber: String): Relative_model?

    @Insert
    suspend fun insert(item: Relative_model)

    @Delete
    suspend fun Delete(item: Relative_model)
}
