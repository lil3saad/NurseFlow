package com.example.nurseflowd1.screens

sealed class TopAppBarState {
    object  AppNameBar : TopAppBarState()
    object  AppNameBack : TopAppBarState()

    object  NurseDashBoard : TopAppBarState()
    object  Profile : TopAppBarState()
    object  NurseNotes : TopAppBarState()
    object  ShitfReport : TopAppBarState()
}