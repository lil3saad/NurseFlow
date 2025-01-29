package com.example.nurseflowd1.presentation.screens.nurseauth

sealed class NurseDocIdState {
    object idle : NurseDocIdState()
    object NoId : NurseDocIdState()
    class CurrentNurseId(var string: String) : NurseDocIdState()
}