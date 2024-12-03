package com.example.nurseflowd1.screens.paitentdash

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.R
import com.example.nurseflowd1.screens.Destinations

@Composable
fun PatientDashBoardScreen(modifier: Modifier = Modifier , viewmodel : AppVM , navcontroller: NavController , patientid: String , patientname : String){

    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("$patientname"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.DefaultBack
    )
    viewmodel.ChangeBottomBarState(barstate = BottomBarState.NoBottomBar)

    Column(modifier = modifier.fillMaxSize().background(AppBg) ,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
        ){
        Text("THIS IS Patient Dash $patientname and there patient id is $patientid")

        Box(contentAlignment = Alignment.BottomEnd) {
            LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).border(1.dp , color = Color.White)) {

            }

            FloatingActionButton( onClick = {
                navcontroller.navigate(Destinations.AddMediceneScreen.ref.replace(oldValue = "{patientid}" , newValue = patientid))
            } ,
                modifier = Modifier.padding(end = 24.dp , bottom = 24.dp),
                shape = CircleShape
            ){
                Icon( imageVector = ImageVector.vectorResource(R.drawable.addpill) , contentDescription = "addpill",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }


    }
    // Display a Vitals Dash , take in the vitals from the user and the display it in a dashboard format

    // Allow the Nurse to set the medication of this current patient 

}