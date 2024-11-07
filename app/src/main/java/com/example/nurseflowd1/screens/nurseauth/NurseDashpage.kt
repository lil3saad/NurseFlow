package com.example.nurseflowd1.screens.nurseauth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DisableContentCapture
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.nurseflowd1.R
import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.domain.AuthState
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.SecClr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// NURSE DASHBOARD
// if a Authenticated user coming from the login screen (AFTER NORMAL LOGIN ) is Authenticated and sent to N_Dashboard and the user keeps pressing back the user will be sent back to the login page , not for the first time , not for second time but he will be surely sent back
@Composable
fun NurseDashBoardScreen(modifier: Modifier , navController: NavController , viewmodel : AppVM) {
    val ScreenHeight = LocalConfiguration.current.screenHeightDp  ; val context = LocalContext.current

    viewmodel.authStatus() // For AutoLogin
    val authState by viewmodel.authstate.collectAsState()
    val gotnursedocid by viewmodel.NurseDocId.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.UnAuthenticated -> {
                navController.navigate(Destinations.LoginScreen.ref)
            }
            is AuthState.Failed -> {
                Toast.makeText(context, (authState as AuthState.Failed).message, Toast.LENGTH_LONG)
                    .show()
            }
            is AuthState.LoadingAuth -> {
                Log.d("TAGY", "Loging in.......")
            }
            is AuthState.Authenticated ->  { if (gotnursedocid == NurseDocIdState.NoId) viewmodel.GetNurseDocId() }
            else -> Unit
        }
    }

    LaunchedEffect(gotnursedocid) {
        if(gotnursedocid is NurseDocIdState.CurrentNurseId) { viewmodel.FetchP_InfoList() }
    } // Just Get NurseId Once & Fetch paitents everytime NDASH Is Launched but only do this once
    Column(modifier = modifier.fillMaxSize().background(AppBg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        val patientinfo by viewmodel.paitientinfolist.collectAsState()
        LazyColumn(
            modifier = Modifier
                .padding(top = (ScreenHeight * 0.09).dp)
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.84f)
        ) {
            when (patientinfo){
                is PatientListState.idlelist -> {
                    item { Text("No patients available. Please add patients.") }
                }
                is PatientListState.PatientsReceived -> {
                    val patientList = (patientinfo as PatientListState.PatientsReceived).patientlist
                    items(patientList) { patient ->
                        val cardPatient = CardPatient(
                            name = "${patient.p_name} ${patient.p_surename}",
                            gender = patient.p_gender,
                            age = patient.p_age,
                            conditon = patient.p_doctor,
                        )
                        PaitentCard(cardPatient)
                    }
                }
                else -> Unit
            }
        }

        BottomNavBar(navController)
    }
}

@Composable
fun PaitentCard(patient : CardPatient ){
    @Composable
    fun ScreenHeight(k : Double) : Double = (LocalConfiguration.current.screenHeightDp * k)
    Card(Modifier.padding( bottom = ScreenHeight(0.02).dp ).border(0.5.dp , color = Color.White.copy(0.11f), shape = RoundedCornerShape(12.dp))
    ){
        ConstraintLayout(modifier = Modifier.height( ScreenHeight(0.16).dp ).width( ScreenHeight(0.9).dp )
            .background(SecClr)
        ){
            val ( p_image , p_details , p_info) = createRefs()

            Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = ScreenHeight(0.01).dp)
                    .fillMaxHeight().fillMaxWidth(0.15f)
                    .constrainAs(p_image){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            ){
                Image(
                    painter = painterResource(R.drawable.user) , contentDescription = "Image",
                    modifier = Modifier.size(  ScreenHeight(0.05).dp )
                )
            }

            Column( verticalArrangement = Arrangement.SpaceEvenly ,
                modifier = Modifier.padding( top = (ScreenHeight(0.02).dp ))
                    .fillMaxHeight(0.9f).fillMaxWidth(0.65f)
                    .constrainAs(p_details){
                        start.linkTo(p_image.end)
                        top.linkTo(parent.top)

                    }
            ){
                Text( "Name : ${patient.name}" , style = TextStyle( fontSize = ScreenHeight(0.022).sp ) , color = Color.White  )
                Text( "Doctor name : ${patient.conditon}" , style = TextStyle( fontSize = ScreenHeight(0.022).sp) , modifier = Modifier.fillMaxWidth() ,  color = Color.White)

                Row( horizontalArrangement = Arrangement.spacedBy( ScreenHeight(0.022).dp ) ,
                    modifier = Modifier.fillMaxWidth()) {
                    Text( "Age : ${patient.age}"  , style = TextStyle( fontSize = ScreenHeight(0.022).sp) , color = Color.White)
                    Text( "Gender: ${patient.gender}", style = TextStyle( fontSize = ScreenHeight(0.022).sp ) , color = Color.White)
                }

            }

            Column( verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight(0.65f)
                    .constrainAs(p_info){
                        start.linkTo(p_details.end)
                        end.linkTo(parent.end , margin = 3.dp)
                        top.linkTo(parent.top , margin = 13.dp )
                        bottom.linkTo(parent.bottom , margin = 13.dp)
                    }
            ){
                Image(
                    painter = painterResource(R.drawable.alarm) , contentDescription = "Image",
                    modifier = Modifier.size(  ScreenHeight(0.04).dp )
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ward" , color = Color.White)
                    Text( text =  "${patient.wardno}"  , color = Color.White)
                }
            }



        }
    }
}


@Composable
fun BottomNavBar(navController: NavController) {
    // BOTTOM BAR
    val ScreenHeight = LocalConfiguration.current.screenHeightDp
    ConstraintLayout(
        modifier = Modifier.padding(bottom = (ScreenHeight * 0.02).dp)
            .fillMaxWidth()
    ) {
        val (btmbar, dcricle, addbtn) = createRefs()

        val btmbarshpae = RoundedCornerShape(45.dp)
        Box(modifier = Modifier.background(color = SecClr, shape = RoundedCornerShape(45.dp))
            .constrainAs(btmbar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .size(width = (ScreenHeight * 0.18).dp, height = (ScreenHeight * 0.055).dp)
            .border(width = 1.dp, shape = btmbarshpae, color = Color.White.copy(alpha = 0.22f))
        ) {
            ConstraintLayout(
                modifier = Modifier.matchParentSize()
                    .background(color = SecClr, shape = btmbarshpae)
                    .padding(top = (ScreenHeight * 0.005).dp)
            ) {
                val (icon1, icon2) = createRefs()
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.constrainAs(icon1) {
                        start.linkTo(parent.start, margin = (ScreenHeight * 0.020).dp)
                        top.linkTo(parent.top) }
                ){
                    // Try Converting a normal image into a vector
                    Image(
                        painter = painterResource(R.drawable.patients),
                        contentDescription = "Paitent Image",
                        modifier = Modifier.size((ScreenHeight * 0.030).dp)
                    )
                    Text("Patients", color = Color.White, fontSize = (ScreenHeight * 0.012).sp)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(
                        start = (ScreenHeight * 0.025).dp,
                        top = (ScreenHeight * 0.003).dp
                    ).constrainAs(icon2) {
                        end.linkTo(parent.end, margin = (ScreenHeight * 0.025).dp)
                        top.linkTo(parent.top)
                    }
                ) {
                    // Try Converting a normal image into a vector
                    Icon(painter = painterResource(R.drawable.user), contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.padding(start = (ScreenHeight * 0.005).dp)
                            .size((ScreenHeight * 0.027).dp)
                            .clickable { navController.navigate(Destinations.AccountScreen.ref) }
                    )

                    Text("User", color = Color.White, fontSize = (ScreenHeight * 0.012).sp)
                }
            }

        }


        val bottomgline = createGuidelineFromBottom(0.30f)
        FloatingActionButton(
            onClick = {}, shape = CircleShape.copy(),
            modifier = Modifier.size((ScreenHeight * 0.07).dp)
                .constrainAs(dcricle) {
                    bottom.linkTo(bottomgline)
                    start.linkTo(btmbar.start)
                    end.linkTo(btmbar.end)

                },
            containerColor = AppBg,
        ) {

        }
        FloatingActionButton(
            onClick = {}, shape = CircleShape.copy(),
            modifier = Modifier
                .size((ScreenHeight * 0.05).dp)
                .constrainAs(addbtn) {
                    start.linkTo(dcricle.start)
                    end.linkTo(dcricle.end)
                    bottom.linkTo(dcricle.bottom, margin = (ScreenHeight * 0.010).dp)
                }.clickable {
                    navController.navigate(Destinations.PatientRegisterScreen.ref)
                },
            containerColor = HTextClr,
            contentColor = Color.White,
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "",
                modifier = Modifier.size((ScreenHeight * 0.05).dp).clickable {
                    navController.navigate(Destinations.PatientRegisterScreen.ref)
                }
            )
        }

    }
}


