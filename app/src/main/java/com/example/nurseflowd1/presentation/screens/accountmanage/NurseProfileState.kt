package com.example.nurseflowd1.presentation.screens.accountmanage

import com.example.nurseflowd1.datalayer.datamodel.NurseInfo

sealed class NurseProfileState {
    object Loading : NurseProfileState()
    object UpdateDone : NurseProfileState()
    data class Fetched(val nurse : NurseInfo) : NurseProfileState()
    data class Failed(val errormsg : String) : NurseProfileState()
}