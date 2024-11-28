package com.example.nurseflowd1.screens.patientboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.datamodels.PatientVitals
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont

@Composable
fun AddVitalsScreen( modifier: Modifier = Modifier , navcontroller : NavController , viewmodel: AppVM){


    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)

    var usertemp  = remember { mutableStateOf("") } ; var usertemp_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var userheartrate  = remember { mutableStateOf("") }; var userheartrate_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var userbp  = remember { mutableStateOf("") }; var userbp_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var userolevels = remember { mutableStateOf("") }; var userolevels_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var user_respirate = remember { mutableStateOf("") }; var repsirate_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var userwardno = remember { mutableStateOf("") }; var userwardno_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var usercondition = remember { mutableStateOf("") }; var condition_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

    var isusercritical = remember { mutableStateOf(false) }


    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll( rememberScrollState() ) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(modifier = Modifier.padding(top = ScreenWidth(0.05).dp).fillMaxHeight().fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            SingupFeilds(
                label = "Temperature",
                textstate = usertemp,
                placeholdertext = "Enter paitent name",
                usertemp_ststate
            )
            SingupFeilds(
                label = "Heart Rate ",
                textstate = userheartrate,
                placeholdertext = "Enter patient surname",
                userheartrate_ststate
            )
            SingupFeilds(
                label = "Blood Pressure",
                textstate = userbp,
                placeholdertext = "Enter patient phone number ",
                userbp_ststate
            )
            SingupFeilds(
                label = "Oxygen Level",
                textstate = userolevels,
                placeholdertext = "Enter kin full name ",
                userolevels_ststate
            )
            SingupFeilds(
                label = "Respiratory rate ",
                textstate = user_respirate,
                placeholdertext = "Enter paitent id assinged by your hosptial  ",
                repsirate_ststate
            )
            SingupFeilds(
                label = "Ward No",
                textstate = userwardno,
                placeholdertext = "Enter the assgined Doctor",
                userwardno_ststate
            )
            SingupFeilds(
                label = "Conditon",
                textstate = usercondition,
                placeholdertext = "Enter the assgined Doctor",
                condition_ststate
            )
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Is your patient in critical condition")
                Switch(checked = isusercritical.value ,
                    onCheckedChange = { ischecked ->
                        isusercritical.value = ischecked
                    },
                    colors = SwitchDefaults.colors(

                    )
                )
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
                    if( usertemp.value.isBlank()){ usertemp_ststate.value = SupportTextState.empty("Required*") ; isvalid = false}
                    else { usertemp_ststate.value = SupportTextState.ideal }

                    if( userheartrate.value.isBlank()){ userheartrate_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { userheartrate_ststate.value = SupportTextState.ideal }

                    if( userbp.value.isBlank()){ userbp_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { userbp_ststate.value = SupportTextState.ideal }

                    if( userolevels.value.isBlank()){ userolevels_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { userolevels_ststate.value = SupportTextState.ideal }

                    if( user_respirate.value.isBlank()){ repsirate_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { repsirate_ststate.value = SupportTextState.ideal }

                    if( userwardno.value.isBlank()){ userwardno_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { userwardno_ststate.value = SupportTextState.ideal }

                    if(usercondition.value.isBlank()){condition_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                    else { condition_ststate.value = SupportTextState.ideal }

                    return isvalid
                }

                if(NotEmptyFeilds()){
                    try {
                        val patientvitals = PatientVitals(
                            iscritical = isusercritical.value,
                            wardno = userwardno.value,
                            condition =  usercondition.value,
                            temp = usertemp.value,
                            heartreate = userheartrate.value,
                            bloodpressure = userbp.value,
                            oxygenlevel = userolevels.value,
                            respiratoryrate = user_respirate.value
                        )
                        val patientinfo = viewmodel.PatientInfo
                        viewmodel.SavePatientInfoFirestore(patientinfo,patientvitals)
                    // Save a Copy of User Entered Patient Info in View Model
                    }catch (e : Exception){
                        errormessage = "Something went Wrong Buddy"
                    }
                }
            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidth (0.04).dp ).size(width = ScreenWidth(0.50).dp , height = ScreenWidth(0.11) .dp)
            ){
                Text("Continue -->" , fontFamily = Headingfont , fontWeight = FontWeight.Bold , fontSize = ScreenWidth (0.05).sp)
            }
        }


    }
}