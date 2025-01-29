package com.example.nurseflowd1.presentation.screens.nursenotes

import com.example.nurseflowd1.datalayer.datamodel.NurseNote


sealed class NurseNoteState {
   object empty : NurseNoteState()
   data class FullNote(val note : NurseNote) : NurseNoteState()
   data class NoteError(val error: String) : NurseNoteState()
}