package com.example.nurseflowd1.room

import androidx.compose.runtime.MutableState
import com.example.nurseflowd1.datamodels.CardPatient

sealed class RoomPatientListState{

    object idle : RoomPatientListState()
    object emptylist : RoomPatientListState()
    object loading : RoomPatientListState()
    object NewAdded : RoomPatientListState()
    data class FetchedList(val patientlist : List<CardPatient>) : RoomPatientListState()

}