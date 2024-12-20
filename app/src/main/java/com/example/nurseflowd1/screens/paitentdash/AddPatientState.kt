package com.example.nurseflowd1.screens.paitentdash

sealed class AddPatientState{
    object idle : AddPatientState()
    object PatientAdded  : AddPatientState()
    object AddingPatient : AddPatientState()
    data class AddPatientFailed(val errormsg : String) : AddPatientState()
}