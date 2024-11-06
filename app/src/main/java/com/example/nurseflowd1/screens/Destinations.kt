package com.example.nurseflowd1.screens

sealed class Destinations(val ref : String) {

    object LoginScreen : Destinations("loginscreen")
    object AuthScreen : Destinations("authscreen")
    object RegisScreen : Destinations("regisscreen")
    object AccountScreen : Destinations("accountscreen")

    object NurseDboardScreen : Destinations("nursedb")
    object PatientRegisterScreen : Destinations("patientinfo")
    object PatientVitalsScreen : Destinations("patientvitals")
}