package com.example.nurseflowd1.room

import com.example.nurseflowd1.datamodels.CardPatient

sealed class RoomPatientListState{

    object idle : RoomPatientListState()
    object emptylist : RoomPatientListState()
    object loading : RoomPatientListState()
    object NewAdded : RoomPatientListState()
    data class FullReadList(val patientlist : List<CardPatient>) : RoomPatientListState()
    data class SearchList(val patientlist : List<CardPatient>) : RoomPatientListState()
    data class CriticalList(val patientlist : List<CardPatient>) : RoomPatientListState()
    data class SortedList(val patientlist : List<CardPatient>) : RoomPatientListState()
    data class Error(val msg : String) : RoomPatientListState()


}