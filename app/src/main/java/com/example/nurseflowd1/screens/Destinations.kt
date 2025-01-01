package com.example.nurseflowd1.screens

sealed class Destinations(val ref : String) {

    object LoginScreen : Destinations("loginscreen")
    object AuthScreen : Destinations("authscreen")
    object RegisScreen : Destinations("regisscreen")

    //Account Settings
    object AccountScreen : Destinations("accountscreen")
    object UpdateProfileScreen : Destinations("updatescreen")
    object AccSettingsScreen: Destinations("settingscreen")

    object NurseDboardScreen : Destinations("nursedb")
    object PatientRegisterScreen : Destinations("patientinfo")

    // Patient DashBoard
    object PatientDashboardScreen : Destinations("patientdash/{patientid}/{patientname}")
    object AddMediceneScreen : Destinations("addmedi/{patientid}/{patientname}")


    object ShiftReportScreen : Destinations("ShiftReport")
    object NurseNotes : Destinations("NurseNotes")


}