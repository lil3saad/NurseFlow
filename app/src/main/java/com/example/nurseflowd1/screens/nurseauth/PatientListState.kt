package com.example.nurseflowd1.screens.nurseauth

import com.example.nurseflowd1.datamodels.PatientInfo

sealed class PatientListState{
    object emptylist : PatientListState()
    object Loadinglist : PatientListState()
    class PatientsReceived(var patientlist : MutableList<PatientInfo>) : PatientListState()
}