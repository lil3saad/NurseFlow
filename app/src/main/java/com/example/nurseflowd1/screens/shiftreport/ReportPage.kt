package com.example.nurseflowd1.screens.shiftreport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.TopAppBarState
import com.example.nurseflowd1.screens.nurseauth.BottomNavBar
import com.example.nurseflowd1.ui.theme.AppBg
import androidx.compose.runtime.getValue

@Composable
fun ShiftReportPage(modifier: Modifier , navController: NavController ,viewmodel : AppVM){

    viewmodel.SetTopBarState(TopAppBarState.ShitfReport)
    Column(modifier = modifier.fillMaxSize().background(AppBg),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("THIS IS SHIFTREPORT PAGE")
        val barstate by viewmodel.topappbarstate.collectAsState()
        BottomNavBar(navController,barstate)
    }
}