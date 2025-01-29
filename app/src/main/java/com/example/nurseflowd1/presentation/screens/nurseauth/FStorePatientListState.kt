package com.example.nurseflowd1.presentation.screens.nurseauth

import com.example.nurseflowd1.datalayer.datamodel.CardPatient

sealed class FStorePatientListState{
    object emptylist : FStorePatientListState()
    object Loadinglist : FStorePatientListState()
    class PatientsReceived(var patientlist : MutableList<CardPatient>) : FStorePatientListState()
}