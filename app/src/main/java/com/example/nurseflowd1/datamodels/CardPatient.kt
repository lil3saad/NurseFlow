package com.example.nurseflowd1.datamodels

data class CardPatient(
    val name : String = "testP",
    val conditon : String = "testc",
    val doctorname : String = "testc",
    val gender : String = "testg",
    val age : String = "",
    val wardno : String = "testw",
    val iscritical : Boolean = false
)
