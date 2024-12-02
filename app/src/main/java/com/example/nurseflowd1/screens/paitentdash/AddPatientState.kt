package com.example.nurseflowd1.screens.paitentdash

sealed class AddPatientState{
    object idle : AddPatientState()
    object AddingPatient : AddPatientState()
    data class AddPatientFailed(val errormsg : String) : AddPatientState()
}