package com.example.nurseflowd1.screens.accountmanage

import com.example.nurseflowd1.datamodels.NurseInfo

sealed class NurseProfileState {
    object Loading : NurseProfileState()
    object UpdateDone : NurseProfileState()
    data class Fetched(val nurse : NurseInfo) : NurseProfileState()
    data class Failed(val errormsg : String) : NurseProfileState()
}