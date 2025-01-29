package com.example.nurseflowd1.datalayer.datamodel

data class PatientInfo(
      var p_firstname : String = "defaultname",
      var p_surename : String = "nosurname",
      var p_patientid : String = "noname",
      var p_gender : String = "noname",
      var condition : String = "nocondition",
      var iscritical : Boolean = false,
      var p_doctor : String = "noname",
      var p_phoneno : String = "noname",
      var wardno : String = "101",
      var p_age : Int = 0,
      val department : String ,
      val admissionDate: Long ,
)