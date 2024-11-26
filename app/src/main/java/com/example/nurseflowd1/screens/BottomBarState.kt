package com.example.nurseflowd1.screens

sealed class BottomBarState {
   object NurseDashBoard : BottomBarState()
   object AccountPage : BottomBarState()
   object ReportsPage : BottomBarState()
   object NotesPage : BottomBarState()
}