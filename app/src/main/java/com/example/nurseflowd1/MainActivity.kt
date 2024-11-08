package com.example.nurseflowd1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nurseflowd1.backend.AppVM
import com.example.nurseflowd1.backend.AuthVMF
import com.example.nurseflowd1.screens.AccountScreen
import com.example.nurseflowd1.screens.nurseauth.AuthScreen
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.nurseauth.LoginScreen
import com.example.nurseflowd1.screens.nurseauth.NurseDashBoardScreen
import com.example.nurseflowd1.screens.nurseauth.NurseRegister
import com.example.nurseflowd1.screens.patientboarding.Paitent_Regis_Screen
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.NurseFlowD1Theme
import com.example.nurseflowd1.ui.theme.jersery25

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ViewModel
        val factory = AuthVMF()
        val viewmodel = ViewModelProvider(this , factory)[AppVM::class.java]
        enableEdgeToEdge()
        setContent {
            NurseFlowD1Theme {
                val Screenwidth = LocalConfiguration.current.screenWidthDp
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Row( verticalAlignment = Alignment.CenterVertically) {
                                    Image( painter = painterResource(R.drawable.syringe) , contentDescription = "" , modifier = Modifier.size( (Screenwidth * 0.10).dp ) )

                                    Text( "NurseFlow" , fontFamily = jersery25  , color = Color.White, fontSize = (Screenwidth * 0.08).sp ,
                                        modifier = Modifier.padding(start = 8.dp, top = 12.dp) )


                                }

                            },
                            colors = TopAppBarColors(
                                containerColor = AppBg ,
                                scrolledContainerColor = Color.Black,
                                navigationIconContentColor = Color.Black,
                                titleContentColor = Color.Black,
                                actionIconContentColor = Color.Black
                            )
                        )
                    }
                ){ innerPadding ->
                    NavigationStack(modifier = Modifier.padding(innerPadding) , viewmodel)
                }
            }
        }
    }
    @Composable
    fun NavigationStack(modifier: Modifier = Modifier , viewmodel : AppVM){
        val navController = rememberNavController()
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
            composable(route = Destinations.PatientRegisterScreen.ref){
                Paitent_Regis_Screen( modifier ,  navController , viewmodel)
            }

        }
    }
}

