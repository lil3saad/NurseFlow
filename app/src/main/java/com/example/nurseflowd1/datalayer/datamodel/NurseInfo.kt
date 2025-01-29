package com.example.nurseflowd1.datalayer.datamodel

data class NurseInfo(
    var N_name : String = "notsetyet",
    var N_surname : String = "notsetyet",
    var N_hospitalname : String = "notsetyet",
    var N_hospitalid : String = "notsetyet",
    var N_council : String = "defaultstate",
    var N_registrationid: String = "notsetyet",
    var N_gender : String = "notsetyet",
    var N_age  : Int = 0,
    var uid : String = "notsetyet",
    var profilepicid : String = "default"
)
