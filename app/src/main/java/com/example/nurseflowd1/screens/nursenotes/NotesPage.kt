package com.example.nurseflowd1.screens.nursenotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.TopAppBarState
import com.example.nurseflowd1.screens.nurseauth.BottomNavBar
import com.example.nurseflowd1.ui.theme.AppBg


@Composable
fun NurseNotesPage(modifier: androidx.compose.ui.Modifier , navController: NavController, viewmodel : AppVM){
    viewmodel.SetTopBarState(TopAppBarState.NurseNotes)
    Column(modifier = modifier.fillMaxSize().background(AppBg),
        Arrangement.SpaceBetween
    ) {
        Text("THIS IS NURSE NOTES PAGE")

        val barstate by viewmodel.topappbarstate.collectAsState()
        BottomNavBar(navController,barstate)
    }
}