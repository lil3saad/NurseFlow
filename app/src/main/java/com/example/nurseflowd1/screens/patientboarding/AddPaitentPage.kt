package com.example.nurseflowd1.screens.patientboarding

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.screens.nurseauth.SignupFeildsSecond
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign


@Composable
fun Paitent_Regis_Screen( modifier: Modifier = Modifier , navcontroller : NavController , viewmodel: AppVM){

    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)

    var user_name  = remember { mutableStateOf("") } ; var username_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var user_surname  = remember { mutableStateOf("") }; var usersurname_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var phoneno  = remember { mutableStateOf("") }; var phoneno_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var kin = remember { mutableStateOf("") }; var kin_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var patient_id = remember { mutableStateOf("") }; var patientid_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var doctorname = remember { mutableStateOf("") }; var doctorname_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var gender = remember { mutableStateOf("") }; var gender_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var age = remember { mutableStateOf("") }; var age_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }


    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll( rememberScrollState() ) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(modifier = Modifier.padding(top = ScreenWidth(0.05).dp).fillMaxHeight().fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            SingupFeilds(
                label = "Firstname",
                textstate = user_name,
                placeholdertext = "Enter paitent name",
                username_ststate
            )
            SingupFeilds(
                label = "Surname",
                textstate = user_surname,
                placeholdertext = "Enter patient surname",
                usersurname_ststate
            )
            SingupFeilds(
                label = "Phone no",
                textstate = phoneno,
                placeholdertext = "Enter patient phone number ",
                phoneno_ststate
            )
            SingupFeilds(
                label = "Kin",
                textstate = kin,
                placeholdertext = "Enter kin full name ",
                kin_ststate
            )
            SingupFeilds(
                label = "Paitent ID",
                textstate = patient_id,
                placeholdertext = "Enter paitent id assinged by your hosptial  ",
                patientid_ststate
            )
            SingupFeilds(
                label = "Doctor",
                textstate = doctorname,
                placeholdertext = "Enter the assgined Doctor",
                doctorname_ststate
            )
            Row(modifier = Modifier.fillMaxWidth().padding( horizontal = ScreenWidth(0.2).dp)
            ){
                SignupFeildsSecond(modifier = Modifier.weight(1f), gender , gender_ststate , "Gender" , false)
                Spacer( modifier = Modifier.size(ScreenWidth(0.1).dp))
                SignupFeildsSecond(modifier = Modifier.weight(0.6f), age , age_ststate , "Age" , true)
            }

            val addPatientState by viewmodel.addPatientState.collectAsState()
            var errormessage by remember { mutableStateOf("") }
            var isloading by remember { mutableStateOf(false) }

            when(val state = addPatientState){
                is AddPatientState.AddPatientFailed -> { isloading = false ; errormessage = state.errormsg }
                AddPatientState.AddingPatient -> { errormessage = "" ; isloading = true }
                AddPatientState.idle -> Unit
            }
            if(errormessage.isNotBlank()){
                Text( text = errormessage,
                    style = TextStyle(fontSize =  ScreenWidth(0.037).sp ),
                    color = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            if(isloading){
                CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
            }

            Button( onClick = {
                fun NotEmptyFeilds() : Boolean { var isvalid = true
                    if( user_name.value.isBlank()){ username_ststate.value = SupportTextState.empty("Required*") ; isvalid = false}
                    else { username_ststate.value = SupportTextState.ideal }

                    if( user_surname.value.isBlank()){ usersurname_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { usersurname_ststate.value = SupportTextState.ideal }

                    if( phoneno.value.isBlank()){ phoneno_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { phoneno_ststate.value = SupportTextState.ideal }

                    if( kin.value.isBlank()){ kin_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { kin_ststate.value = SupportTextState.ideal }

                    if( patient_id.value.isBlank()){ patientid_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { patientid_ststate.value = SupportTextState.ideal }

                    if( doctorname.value.isBlank()){ doctorname_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { doctorname_ststate.value = SupportTextState.ideal }

                    if(gender.value.isBlank()){gender_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                    else { gender_ststate.value = SupportTextState.ideal }

                    if(age.value.isBlank()){age_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                    else { age_ststate.value = SupportTextState.ideal }

                    return isvalid
                }

                if(NotEmptyFeilds()){
                    try {
                        val patientinfo = PatientInfo(
                            p_name =  user_name.value, p_surename = user_surname.value ,
                            p_phoneno = phoneno.value,
                            p_patientid = patient_id.value ,
                            p_doctor = doctorname.value , p_age = age.value.toInt() , p_gender = gender.value
                        )
                        viewmodel.SavePatientInfoFirestore(patientinfo)
                    }catch (e : Exception){
                        errormessage = "Age can only have numbers"
                    }
                }
            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidth (0.04).dp ).size(width = ScreenWidth(0.50).dp , height = ScreenWidth(0.11) .dp)
            ){
                Text("Continue -->" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = ScreenWidth (0.05).sp)
            }
        }


    }
}