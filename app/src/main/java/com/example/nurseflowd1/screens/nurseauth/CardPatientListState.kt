package com.example.nurseflowd1.screens.nurseauth

import com.example.nurseflowd1.datamodels.CardPatient

sealed class CardPatientListState{
    object emptylist : CardPatientListState()
    object Loadinglist : CardPatientListState()
    class PatientsReceived(var patientlist : MutableList<CardPatient>) : CardPatientListState()
}