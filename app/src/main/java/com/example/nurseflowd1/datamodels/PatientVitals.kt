package com.example.nurseflowd1.datamodels

data class PatientVitals(
    var  iscritical : Boolean = false,
    var  wardno : String  = "G-101",
    var condition : String = "Diarreaha",

    var temp : String = "tempnotset" ,
    var heartreate : String = "hrnotset" ,
    var bloodpressure : String = "bpnotset",
    var oxygenlevel : String = "o2notset",
    var respiratoryrate : String = "respiratorynotset"
)