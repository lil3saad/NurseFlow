package com.example.nurseflowd1.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medstable" , primaryKeys = ["medid","dosageno"])
data class MedieneInfo(
    val medid  : String,
    val dosageno : Int,
    val medi_name : String ,
    val medi_type : String ,
    val patientid : String,
    val patientname : String,
    val meditime :  String,
    val endmeditime: Long,
)