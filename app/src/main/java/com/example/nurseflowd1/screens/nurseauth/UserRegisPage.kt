package com.example.nurseflowd1.screens.nurseauth

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.datamodels.NurseRegisInfo
import com.example.nurseflowd1.backend.AppVM
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25


@Composable
fun NurseRegister(modifier: Modifier = Modifier, navController: NavController , viewmodel : AppVM){
    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)

    var user_name  = remember { mutableStateOf("") } ; var username_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var user_surname  = remember { mutableStateOf("") } ; var usersurname_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var hospital_name  = remember { mutableStateOf("") }; var hospitalname_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var hospital_id  = remember { mutableStateOf("") }; var hospitalid_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var nurse_license_id  = remember { mutableStateOf("") }; var nurselicense_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var gender = remember { mutableStateOf("") }; var gender_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var age = remember { mutableStateOf("") }; var age_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally){

        Column(modifier = Modifier.padding(top = ScreenWidth(0.3).dp )
            .fillMaxWidth(0.9f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            SingupFeilds(
                label = "Name",
                textstate = user_name,
                placeholdertext = "Enter your firstname",
                username_ststate
            )
            SingupFeilds(
                label = "Surname",
                textstate = user_surname,
                placeholdertext = "Enter your lastname",
                usersurname_ststate
            )
            SingupFeilds(
                label = "Hospital",
                textstate = hospital_name,
                placeholdertext = "Enter your hospital name",
                hospitalname_ststate
            )
            SingupFeilds(
                label = "Hospital ID",
                textstate = hospital_id,
                placeholdertext = "Enter Id provided by your hospital",
                hospitalid_ststate
            )
            SingupFeilds(
                label = "License ID",
                textstate = nurse_license_id,
                placeholdertext = "Enter your Unique License id provided by Nursing Body",
                nurselicense_ststate
            )


            Row(modifier = Modifier.fillMaxWidth().padding( horizontal = ScreenWidth(0.2).dp)
            ){
                SignupFeildsSecond(modifier = Modifier.weight(1f), gender , gender_ststate , "Gender")
                Spacer( modifier = Modifier.size(ScreenWidth(0.1).dp))
                SignupFeildsSecond(modifier = Modifier.weight(0.6f), age , age_ststate , "Age")
            }
            Button( onClick = {
                    fun NotEmptyFeilds() : Boolean { var isvalid = true
                        if( user_name.value.isBlank()){ username_ststate.value = SupportTextState.empty("Required*") ; isvalid = false}
                        else { username_ststate.value = SupportTextState.ideal }

                        if( user_surname.value.isBlank()){ usersurname_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                        else { usersurname_ststate.value = SupportTextState.ideal }

                        if( hospital_name.value.isBlank()){ hospitalname_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                        else { hospitalname_ststate.value = SupportTextState.ideal }

                        if( hospital_id.value.isBlank()){ hospitalid_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                        else {hospitalid_ststate.value = SupportTextState.ideal }

                        if( nurse_license_id.value.isBlank()){ nurselicense_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                        else { nurselicense_ststate.value = SupportTextState.ideal }

                        if(gender.value.isBlank()){gender_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                        else { gender_ststate.value = SupportTextState.ideal }

                        if(age.value.isBlank()){age_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                        else { age_ststate.value = SupportTextState.ideal }

                        return isvalid
                    }
                    if (NotEmptyFeilds()){
                        val nurseinfo = NurseRegisInfo( N_name = user_name.value , N_surname =  user_surname.value ,
                            N_hospitalname =  hospital_name.value, N_hospitalid = hospital_id.value ,
                            N_license_id = nurse_license_id.value , N_gender = gender.value , N_age = age.value.toInt()
                        )
                        viewmodel.SaveNurseInfo(nurseinfo)
                        navController.navigate( route = Destinations.AuthScreen.ref)
                    }
            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidth (0.04).dp ).size(width = ScreenWidth(0.50).dp , height = ScreenWidth(0.11) .dp)
            ) {
                Text("Finish" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = ScreenWidth (0.05).sp)
            }
        }


    }
}
@Composable
fun SignupFeildsSecond( modifier: Modifier,
    textstate : MutableState<String> , supporttextstate : MutableState<SupportTextState> ,
    placeholder : String
){
    val iserror = remember { mutableStateOf(false) }
    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)
    val softwarekeyboard = GiveKeyboard()
        OutlinedTextField( value = textstate.value , onValueChange = {
                it -> textstate.value = it
        },
            placeholder = { Text(placeholder , color = Color.White.copy(0.50f) )},
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    softwarekeyboard.hide()
                }
            ),
            supportingText = {
                when(val state = supporttextstate.value) {
                    is SupportTextState.empty ->  { Text(state.errormsg) ; iserror.value = true}
                    is SupportTextState.ideal -> { iserror.value = false }
                    else -> Unit
                }
            },
            isError = iserror.value
        )
}

