package com.example.nurseflowd1.presentation.screens.nursenotes

import com.example.nurseflowd1.datalayer.datamodel.NurseNote

sealed class RoomNoteListState {
    object EmptyList : RoomNoteListState()
    data class Receive(val list : List<NurseNote>) : RoomNoteListState()
    data class ListError(val error : String) : RoomNoteListState()
    object Loading : RoomNoteListState()
}