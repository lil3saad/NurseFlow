package com.example.nurseflowd1.screens.patientboarding

sealed class PaitentBoardingState {
    object Loading : PaitentBoardingState()
    object InfoRegistered : PaitentBoardingState()
    object VitalsRegistered : PaitentBoardingState()
    class Failed(val errormessage : String) : PaitentBoardingState()
}