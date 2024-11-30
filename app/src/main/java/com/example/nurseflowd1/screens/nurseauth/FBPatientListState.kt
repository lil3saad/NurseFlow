package com.example.nurseflowd1.screens.nurseauth

import com.example.nurseflowd1.datamodels.CardPatient

sealed class FBPatientListState{
    object emptylist : FBPatientListState()
    object Loadinglist : FBPatientListState()
    class PatientsReceived(var patientlist : MutableList<CardPatient>) : FBPatientListState()
}