package com.example.nurseflowd1

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.nurseflowd1.domain.usecases.AWStorageUseCase
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.NurseFlowD1Theme
import com.example.nurseflowd1.ui.theme.Headingfont
import io.appwrite.Client
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.nurseflowd1.datalayer.room.RoomDB
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.domain.usecases.RoomMediUC
import com.example.nurseflowd1.domain.usecases.NurseNoteUC
import com.example.nurseflowd1.domain.usecases.RoomPatientUC
import com.example.nurseflowd1.presentation.AppBarColorState
import com.example.nurseflowd1.presentation.AppBarTitleState
import com.example.nurseflowd1.presentation.BottomBarState
import com.example.nurseflowd1.presentation.Destinations
import com.example.nurseflowd1.presentation.NavigationIconState
import com.example.nurseflowd1.presentation.screens.accountmanage.AccountScreen
import com.example.nurseflowd1.presentation.screens.accountmanage.AccountSettingPage
import com.example.nurseflowd1.presentation.screens.accountmanage.UpdateProfilePage
import com.example.nurseflowd1.presentation.screens.nurseauth.AuthScreen
import com.example.nurseflowd1.presentation.screens.nurseauth.LoginScreen
import com.example.nurseflowd1.presentation.screens.nurseauth.MyBottomNavBar
import com.example.nurseflowd1.presentation.screens.nurseauth.NurseDashBoardScreen
import com.example.nurseflowd1.presentation.screens.nurseauth.NurseRegister
import com.example.nurseflowd1.presentation.screens.nursenotes.AddNurseNote
import com.example.nurseflowd1.presentation.screens.nursenotes.NurseNotesPage
import com.example.nurseflowd1.presentation.screens.paitentdash.Add_PatientInfo_Screen
import com.example.nurseflowd1.presentation.screens.paitentdash.PatientDashBoardScreen
import com.example.nurseflowd1.presentation.screens.paitentdash.medication.AddMedScreen
import com.example.nurseflowd1.presentation.screens.shiftreport.ReportPage
import com.example.nurseflowd1.ui.theme.HTextClr
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {


            val viewmodel = getViewModel<AppVM>()

            val notimanager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val navController = rememberNavController()

            NurseFlowD1Theme {
                val Screenwidth = LocalConfiguration.current.screenWidthDp
                Scaffold(modifier = Modifier.background(HTextClr).systemBarsPadding()
                    .fillMaxSize(),
                    topBar = {
                        val titlestate by viewmodel.appbartitlestate.collectAsState()
                        val iconstate by viewmodel.appbariconstate.collectAsState()
                        val colorstate by viewmodel.appbarcolorstate.collectAsState()
                        Surface(shape = RoundedCornerShape(bottomStart = 45.dp , bottomEnd = 45.dp), // Customize the shape
                            color = HTextClr, // Background color for the app bar
                            modifier = when(colorstate) {
                                AppBarColorState.DefaultColors -> { Modifier.fillMaxWidth().background(AppBg) }
                                AppBarColorState.NurseDashColors -> { Modifier.fillMaxWidth().background(HTextClr) }
                            }
                        ){
                            TopAppBar(  modifier = Modifier.padding(horizontal = 12.dp , vertical = 6.dp),
                                title = { when (val state = titlestate) {
                                        is AppBarTitleState.DisplayTitle -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(R.drawable.syringe),
                                                    contentDescription = "",
                                                    modifier = Modifier.size((Screenwidth * 0.10).dp)
                                                )
                                                Text(
                                                    state.display,
                                                    fontFamily = Headingfont,
                                                    color = Color.White,
                                                    fontSize = 28.sp,
                                                    modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                                                )
                                            }
                                        }
                                        is AppBarTitleState.DisplayTitleWithBack -> Unit
                                        AppBarTitleState.NoTopAppBar -> Unit
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent, // Set to transparent to avoid overlap with Surface color
                                    titleContentColor = Color.White,

                                ),
                                navigationIcon = {
                                    when (iconstate) {
                                        NavigationIconState.DefaultBack -> {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "NavigationIcon",
                                                modifier = Modifier
                                                    .padding(start = 4.dp, end = 12.dp, top = 12.dp)
                                                    .size((Screenwidth * 0.08).dp)
                                                    .clickable { navController.popBackStack() },
                                                tint = Color.White
                                            )
                                        }
                                        NavigationIconState.None -> Unit
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        val bottombarstate by viewmodel.bottombarstate.collectAsState()
                        when(val state = bottombarstate){
                            BottomBarState.NurseDashBoard -> {
                                MyBottomNavBar(navController, state)
                            }
                            BottomBarState.NotesPage -> {
                                MyBottomNavBar(navController, state)
                            }
                            BottomBarState.ReportsPage -> {
                                MyBottomNavBar(navController, state)
                            }
                            BottomBarState.AccountPage -> {
                                MyBottomNavBar(navController, state)
                            }
                            BottomBarState.FlatNavigation -> {
                                MyBottomNavBar(navController, state)
                            }
                            BottomBarState.PaitentDash -> {
                                MyBottomNavBar(navController, state)
                            }
                            BottomBarState.NoBottomBar -> {
                                MyBottomNavBar(navController, state)
                            }
                        }
                    },
                ){ innerPadding ->
                    NavigationStack(modifier = Modifier.padding(innerPadding) , navController , viewmodel , notimanager)
                }
            }
        }
    }

    @Composable
    fun NavigationStack(modifier: Modifier = Modifier, navController: NavHostController, viewmodel : AppVM, notimanager: NotificationManager){
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
                Add_PatientInfo_Screen( modifier ,  navController , viewmodel)
            }
            composable( route = Destinations.ShiftReportScreen.ref){
                ReportPage(modifier , navController, viewmodel)
            }
            composable( route = Destinations.NurseNotes.ref){
                NurseNotesPage(modifier , navController, viewmodel)
            }
            composable(route = Destinations.AddNoteScreen.route , arguments = listOf(
                navArgument("notetitle"){  nullable = true ;  type = NavType.StringType },
                navArgument("notebody"){   nullable = true ; type = NavType.StringType } ,
                navArgument("UpdateNote") {  defaultValue = false ; type = NavType.BoolType },
                navArgument("NoteId") {  defaultValue = 0 ; type = NavType.LongType }
            )  ){  navstackentries ->


                val noteid = navstackentries.arguments!!.getLong("NoteId")
                val isupdatenote = navstackentries.arguments!!.getBoolean("UpdateNote")

                val notetittle = navstackentries.arguments!!.getString("notetitle")
                val notebody = navstackentries.arguments!!.getString("notebody")

                AddNurseNote(modifier , viewmodel , navController , isupdatenote , noteid , notetittle!! , notebody!!)

            }
            composable( route = Destinations.PatientDashboardScreen.ref , arguments = listOf(
                navArgument(name = "patientid"){
                    defaultValue = "patientidnotpassed"
                    type = NavType.StringType
                } ,
                navArgument(name = "patientname"){
                    defaultValue = "nopaitentnamepassed"
                    type = NavType.StringType
                }
            )){
                navbackstackentry ->
                val patientid = navbackstackentry.arguments!!.getString("patientid")!!
                val patientname = navbackstackentry.arguments!!.getString("patientname")!!
                PatientDashBoardScreen(modifier,viewmodel, navController , patientid , patientname )
            }
            composable(route = Destinations.AddMediceneScreen.ref , arguments = listOf(
                navArgument(name = "patientid" ) {
                    defaultValue = "idnotpassed"
                    type = NavType.StringType
                },
                navArgument("patientname") {
                    defaultValue = "notpatientname"
                    type = NavType.StringType
                }
            )){ navbackstackentry ->
                val patientid = navbackstackentry.arguments!!.getString("patientid")!!
                val patientname = navbackstackentry.arguments!!.getString("patientname")!!
                AddMedScreen(modifier , navController, viewmodel ,patientid , patientname )
            }
            composable(route = Destinations.AddReportScreen.ref){

                ReportPage(modifier,navController,viewmodel)
            }

        }
    }
}

