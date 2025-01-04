package com.example.nurseflowd1.screens.nursenotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.ui.theme.AppBg


@Composable
fun NurseNotesPage(modifier: androidx.compose.ui.Modifier , navController: NavController, viewmodel : AppVM){
    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("Notes"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.None
    )
    viewmodel.ChangeBottomBarState(BottomBarState.NotesPage)
    Column(modifier = modifier
        .background(AppBg)
        .fillMaxSize(),
        Arrangement.SpaceBetween
    ) {
        Text("THIS IS NURSE NOTES PAGE" , color =  Color.DarkGray )
    }
}