package com.example.nurseflowd1.datalayer.datamodel

data class PatientVitals(
    val vitalreport_time : String,
    var bloodpressure : String = "bpnotset",
    var heartreate : String = "hrnotset",
    var temp : String = "tempnotset",
    var oxygenlevel : String = "o2notset",
    var respiratoryrate : String = "respiratorynotset",
)