package com.example.nurseflowd1.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patientcards")
data class PatientCardEntity(
    @PrimaryKey()
    @ColumnInfo(name = "Patientid")
    val patientid : String,

    @ColumnInfo(name = "Name")
    val name : String,
    @ColumnInfo()
    val condition : String,
    @ColumnInfo()
    val doctorname : String,
    @ColumnInfo
    val gender : String,
    @ColumnInfo
    val age : String,
    @ColumnInfo
    val wardno : String,
    @ColumnInfo
    val iscrictal : Boolean




)