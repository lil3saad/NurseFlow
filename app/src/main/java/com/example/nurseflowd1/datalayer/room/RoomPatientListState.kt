package com.example.nurseflowd1.datalayer.room

import com.example.nurseflowd1.datalayer.datamodel.CardPatient

sealed class RoomPatientListState{

    object idle : RoomPatientListState()
    object emptylist : RoomPatientListState()
    object loading : RoomPatientListState()
    object NewAdded : RoomPatientListState()
    data class FetchedList(val patientlist : List<CardPatient>) : RoomPatientListState()
    data class Error(val msg : String) : RoomPatientListState()


}