package com.example.nurseflowd1.screens.nurseauth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.datamodels.NurseRegisInfo
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25


@Composable
fun NurseRegister(modifier: Modifier = Modifier, navController: NavController , viewmodel : AppVM){
    @Composable
    fun ScreenWidhth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)

    var user_name  = remember { mutableStateOf("") }
    var user_surname  = remember { mutableStateOf("") }
    var hospital_name  = remember { mutableStateOf("") }
    var hospital_id  = remember { mutableStateOf("") }
    var nurse_license_id  = remember { mutableStateOf("") }
    var gender = remember { mutableStateOf("") }
    var age = remember { mutableStateOf("") }

    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally){

        Column(modifier = Modifier.padding(top = ScreenWidhth(0.3).dp )
            .fillMaxWidth(0.9f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            SingupFeilds(
                label = "Name",
                textstate = user_name,
                placeholdertext = "Enter your firstname"
            )
            SingupFeilds(
                label = "Surname",
                textstate = user_surname,
                placeholdertext = "Enter your lastname"
            )
            SingupFeilds(
                label = "Hospital",
                textstate = hospital_name,
                placeholdertext = "Enter your hospital name"
            )
            SingupFeilds(
                label = "Hospital ID",
                textstate = hospital_id,
                placeholdertext = "Enter Id provided by your hospital"
            )
            SingupFeilds(
                label = "License ID",
                textstate = nurse_license_id,
                placeholdertext = "Enter your Unique License id provided by Nursing Body"
            )

            val softwarekeyboard = GiveKeyboard()
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedTextField( value = gender.value , onValueChange = {
                        it -> gender.value = it
                },
                    placeholder = { Text("Gender" , color = Color.White.copy(0.50f) )},
                    modifier = Modifier.fillMaxWidth(0.45f),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            softwarekeyboard.hide()
                        }
                    )
                )
                Spacer(modifier = Modifier.size( ScreenWidhth(0.02).dp ))
                OutlinedTextField( value = age.value , onValueChange = {
                        it -> age.value = it
                },
                    placeholder = { Text("Age" , color = Color.White.copy(0.50f) )},
                    modifier = Modifier.fillMaxWidth(0.45f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            softwarekeyboard.hide()
                        }
                    )
                )

            }
            val context = LocalContext.current
            Button( onClick = {
                try {
                    val nurseinfo = NurseRegisInfo( N_name = user_name.value , N_surname =  user_surname.value ,
                        N_hospitalname =  hospital_name.value, N_hospitalid = hospital_id.value ,
                        N_license_id = nurse_license_id.value , N_gender = gender.value , N_age = age.value.toInt()
                    )
                    viewmodel.SaveNurseInfo(nurseinfo)
                    navController.navigate( route = Destinations.AuthScreen.ref)
                }catch (e : Exception){
                    // Handle Button Clicked with any values Entered
                    Toast.makeText(context,"the error is $e" , Toast.LENGTH_LONG).show()
                }

            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidhth (0.04).dp ).size(width = ScreenWidhth(0.50).dp , height = ScreenWidhth(0.11) .dp)
            ) {
                Text("Finish" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = ScreenWidhth (0.05).sp)
            }
        }


    }
}