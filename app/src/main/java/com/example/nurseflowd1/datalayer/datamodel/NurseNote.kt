package com.example.nurseflowd1.datalayer.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nursenotes")
data class NurseNote(
    @PrimaryKey
    val noteid : Long,
    val title : String,
    val body : String
)