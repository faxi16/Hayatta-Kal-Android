package com.maherlabbad.hayattakal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Relative_model(
    @ColumnInfo("Name") var name: String,
    @ColumnInfo("Phone_number") var phone_number: String,
    @PrimaryKey(autoGenerate = true)
    var Id : Int = 0)
