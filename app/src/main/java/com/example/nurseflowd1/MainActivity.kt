package com.example.nurseflowd1

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nurseflowd1.domain.AuthVMF
import com.example.nurseflowd1.domain.usecases.AWStorageUseCase
import com.example.nurseflowd1.screens.accountmanage.AccountScreen
import com.example.nurseflowd1.screens.nurseauth.AuthScreen
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.accountmanage.AccountSettingPage
import com.example.nurseflowd1.screens.accountmanage.UpdateProfilePage
import com.example.nurseflowd1.screens.nurseauth.LoginScreen
import com.example.nurseflowd1.screens.nurseauth.NurseDashBoardScreen
import com.example.nurseflowd1.screens.nurseauth.NurseRegister
import com.example.nurseflowd1.screens.patientboarding.Paitent_Regis_Screen
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.NurseFlowD1Theme
import com.example.nurseflowd1.ui.theme.Headingfont
import io.appwrite.Client
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import com.example.nurseflowd1.domain.usecases.RoomUseCase
import com.example.nurseflowd1.room.RoomDB
import com.example.nurseflowd1.screens.TopAppBarState
import com.example.nurseflowd1.screens.nurseauth.BottomNavBar
import com.example.nurseflowd1.screens.nursenotes.NurseNotesPage
import com.example.nurseflowd1.screens.patientboarding.AddVitalsScreen
import com.example.nurseflowd1.screens.shiftreport.ShiftReportPage
import com.example.nurseflowd1.ui.theme.panelcolor


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // NOT WORKING

        val client : Client = Client(this).setEndpoint("https://cloud.appwrite.io/v1").setProject("673b1afc002275ec3f3a")

        val patientdao  = RoomDB.invoke(this).getpatientcardDAO()

        val roomuse = RoomUseCase(patientdao)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val navController = rememberNavController()
            val factory = AuthVMF(navController , AWStorageUseCase(client, context = LocalContext.current) , roomuse)
            val viewmodel = ViewModelProvider(this , factory)[AppVM::class.java]

            NurseFlowD1Theme {
                val Screenwidth = LocalConfiguration.current.screenWidthDp
                val topbarstate by viewmodel.topappbarstate.collectAsState()

                Scaffold(modifier = Modifier.background(AppBg)
                    .systemBarsPadding() // LOAD SCAFFOLD RESPECTING THE USER'S GESTURES / BUTTON SYSTEM BARS
                    .fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                when(topbarstate){
                                    TopAppBarState.AppNameBar -> {
                                        Row( verticalAlignment = Alignment.CenterVertically) {
                                            Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )
                                            Text( "NurseFlow" , fontFamily = Headingfont  , color = Color.White, fontSize = (Screenwidth * 0.08).sp ,
                                                modifier = Modifier.padding(start = 8.dp, top = 12.dp) )
                                        }
                                    }
                                    TopAppBarState.AppNameBack -> {
                                        Row( verticalAlignment = Alignment.CenterVertically) {
                                            Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )
                                            Text( "NurseFlow" , fontFamily = Headingfont  , color = Color.White, fontSize = (Screenwidth * 0.08).sp ,
                                                modifier = Modifier.padding(start = 8.dp, top = 12.dp) )
                                        }
                                    }
                                    TopAppBarState.NurseDashBoard -> {
                                        Row( verticalAlignment = Alignment.CenterVertically) {
                                            Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )
                                            Text( "DashBoard" , fontFamily = Headingfont  , color = Color.White, fontSize = (Screenwidth * 0.08).sp , fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(start = 8.dp, top = 12.dp) )
                                        }
                                    }
                                    TopAppBarState.Profile -> {
                                        Row( verticalAlignment = Alignment.CenterVertically) {
                                            Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )
                                            Text( "Profile" , fontFamily = Headingfont  , color = Color.White, fontSize = (Screenwidth * 0.08).sp ,
                                                modifier = Modifier.padding(start = 8.dp, top = 12.dp) )
                                        }
                                    }
                                    TopAppBarState.ShitfReport -> {
                                        Row( verticalAlignment = Alignment.CenterVertically) {
                                            Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )
                                            Text( "Shift Reports" , fontFamily = Headingfont  , color = Color.White, fontSize = (Screenwidth * 0.08).sp ,
                                                modifier = Modifier.padding(start = 8.dp, top = 12.dp) )
                                        }
                                    }
                                    TopAppBarState.NurseNotes -> {
                                        Row( verticalAlignment = Alignment.CenterVertically) {
                                            Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )
                                            Text( "Notes" , fontFamily = Headingfont  , color = Color.White, fontSize = (Screenwidth * 0.08).sp ,
                                                modifier = Modifier.padding(start = 8.dp, top = 12.dp) )
                                        }
                                    }

                                    TopAppBarState.DoNotDisplay -> {
                                        Unit
                                    }
                                }
                            },
                            colors = when(topbarstate){
                                TopAppBarState.NurseDashBoard -> {
                                    TopAppBarColors(
                                        containerColor = panelcolor,
                                        scrolledContainerColor = Color.Black,
                                        navigationIconContentColor = Color.Black,
                                        titleContentColor = Color.Black,
                                        actionIconContentColor = Color.Black
                                    )
                                }
                                else -> {
                                    TopAppBarColors(
                                        containerColor = AppBg,
                                        scrolledContainerColor = Color.Black,
                                        navigationIconContentColor = Color.Black,
                                        titleContentColor = Color.Black,
                                        actionIconContentColor = Color.Black
                                    )
                                }
                            }
                            ,
                            navigationIcon = {
                                when(topbarstate){
                                    TopAppBarState.AppNameBack -> {
                                        Icon(imageVector = Icons.Default.ArrowBack , contentDescription = "NavigationIcon",
                                            modifier = Modifier.padding(start = 4.dp , end = 12.dp, top = 12.dp)
                                                .size( (Screenwidth * 0.08).dp )
                                                .clickable{ navController.popBackStack() },
                                            tint = Color.White
                                        )
                                    }
                                    else -> Unit
                                }
                            }

                        )
                    },
                    bottomBar = { val barstate by viewmodel.topappbarstate.collectAsState()
                        BottomNavBar(navController , barstate)
                    }
                ){ innerPadding ->
                    NavigationStack(modifier = Modifier.padding(innerPadding) , navController , viewmodel)
                }
            }
        }
    }
    @Composable
    fun NavigationStack(modifier: Modifier = Modifier , navController: NavHostController , viewmodel : AppVM){
        NavHost( navController = navController , startDestination = Destinations.NurseDboardScreen.ref){
            composable(route = Destinations.LoginScreen.ref){
                LoginScreen(modifier, navController , viewmodel)
            }
            composable(route = Destinations.AuthScreen.ref){
                AuthScreen(modifier = modifier, navController , viewmodel)
            }
            composable(route = Destinations.RegisScreen.ref){
                NurseRegister(modifier, navController, viewmodel)
            }
            composable(route = Destinations.NurseDboardScreen.ref ,
            ){
                NurseDashBoardScreen(modifier, navController , viewmodel)
            }
            composable(route = Destinations.AccountScreen.ref){
                AccountScreen(modifier , viewmodel , navController)
            }
            composable(route = Destinations.UpdateProfileScreen.ref){
                UpdateProfilePage(modifier , viewmodel = viewmodel , navController = navController)
            }
            composable(route = Destinations.AccSettingsScreen.ref){
                AccountSettingPage(modifier)
            }
            composable(route = Destinations.PatientRegisterScreen.ref){
                Paitent_Regis_Screen( modifier ,  navController , viewmodel)
            }
            composable( route = Destinations.ShiftReportScreen.ref){
                ShiftReportPage(modifier , navController, viewmodel)
            }
            composable( route = Destinations.NurseNotes.ref){
                NurseNotesPage(modifier , navController, viewmodel)
            }
            composable(route = Destinations.PatientVitalsScreen.ref){
                AddVitalsScreen(modifier,navController,viewmodel)
            }


        }
    }
}

