package com.example.nurseflowd1.presentation

sealed class BottomBarState {
   object NurseDashBoard : BottomBarState()
   object AccountPage : BottomBarState()
   object ReportsPage : BottomBarState()
   object NotesPage : BottomBarState()

   object FlatNavigation : BottomBarState()
   object PaitentDash : BottomBarState()

   object NoBottomBar : BottomBarState()
}