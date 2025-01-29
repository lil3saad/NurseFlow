package com.example.nurseflowd1.datalayer.datamodel

data class PatientShiftReport(
   // Patient Id + Int.Random()

    val shiftid : String,

    val shift_startime : String,
    val shift_endtime : String,
    val shift_date : String,


    val pInfo: P_ReportInfo,
    val patientMedicalInfo: P_ReportMedicalInfo,

    val pre_operative_order : String,
    val post_operative_ordre : String,
    val procedure_status : String,
    val stat_medication : String,
    val doc_verbal_order_name : String,
    val nurse_remarks : String
)

data class P_ReportInfo(
    val patientname : String,
    val patientid : String,
    val Gender : String ,
    val wardno : String,
    val dob : String
)
data class P_ReportMedicalInfo(
    val doctors : String,
    val diagnosis : String,
    val medicalhistory : String,
    val allergies : String,
    val painlevels : String = "nopainlevels set",
    val vitals_timelist : List<PatientVitals>
)
