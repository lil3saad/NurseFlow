package com.example.nurseflowd1.screens.paitentdash.medication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.nurseflowd1.screens.nurseauth.SignupFeildsSecond
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import com.example.nurseflowd1.ui.theme.AppBg

@Composable
fun AddMedScreen(modifier: Modifier = Modifier , viewmodel : AppVM , patientid : String){
    Column(modifier = modifier.fillMaxSize().background(AppBg)) {
        val user_mediname = remember { mutableStateOf("") }
        var user_medinamestate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
        SingupFeilds(
            label = patientid,
            user_mediname,
            placeholdertext = "Enter Medicene Name",
            user_medinamestate
        )
        LazyColumn {

        }
    }
}
