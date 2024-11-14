package com.example.nurseflowd1.screens.accountmanage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr


@Composable
fun UpdateProfilePage(modifier: Modifier){
    Column(modifier = modifier.fillMaxSize()
        .background(AppBg)){
        Text("UPDATE PROFILE PAGE" , fontSize = 25.sp , color = HTextClr)
    }
}