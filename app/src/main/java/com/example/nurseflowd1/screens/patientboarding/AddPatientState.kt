package com.example.nurseflowd1.screens.patientboarding

sealed class AddPatientState{
    object idle : AddPatientState()
    object AddingPatient : AddPatientState()
    data class AddPatientFailed(val errormsg : String) : AddPatientState()
}