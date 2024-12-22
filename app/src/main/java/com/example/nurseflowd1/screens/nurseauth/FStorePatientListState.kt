package com.example.nurseflowd1.screens.nurseauth

import com.example.nurseflowd1.datamodels.CardPatient

sealed class FStorePatientListState{
    object emptylist : FStorePatientListState()
    object Loadinglist : FStorePatientListState()
    class PatientsReceived(var patientlist : MutableList<CardPatient>) : FStorePatientListState()
}