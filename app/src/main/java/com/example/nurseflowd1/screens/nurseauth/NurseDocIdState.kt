package com.example.nurseflowd1.screens.nurseauth

sealed class NurseDocIdState {
    object idle : NurseDocIdState()
    object NoId : NurseDocIdState()
    class CurrentNurseId(var string: String) : NurseDocIdState()
}