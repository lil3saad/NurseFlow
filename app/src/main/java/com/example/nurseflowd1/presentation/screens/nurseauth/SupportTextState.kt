package com.example.nurseflowd1.presentation.screens.nurseauth

sealed class SupportTextState {
    object ideal : SupportTextState()
    class  empty(val errormsg : String)  : SupportTextState()
    class  invalid(val errormsg : String) : SupportTextState()
}