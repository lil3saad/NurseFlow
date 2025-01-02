package com.example.nurseflowd1.screens.shiftreport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.ui.theme.AppBg
import androidx.compose.runtime.getValue
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState

@Composable
fun ShiftReportPage(modifier: Modifier , navController: NavController ,viewmodel : AppVM){
    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("ShiftReports"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.None
    )
    viewmodel.ChangeBottomBarState(BottomBarState.ReportsPage)
    Column(modifier = modifier.fillMaxSize().background(AppBg),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Text("THIS IS SHIFTREPORT PAGE")
        val barstate by viewmodel.appbartitlestate.collectAsState()
    }
}