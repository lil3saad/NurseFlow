package com.example.nurseflowd1.screens.paitentdash

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.nurseflowd1.ui.theme.Headingfont
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.screens.nurseauth.SignupFeildsSecond
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.NavigationIconState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import java.util.Calendar


@Composable
fun Add_PatientInfo_Screen(modifier: Modifier = Modifier, navcontroller : NavController, viewmodel: AppVM){

    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("Patient Admission"),
        colorState = AppBarColorState.DefaultColors,
        iconState = NavigationIconState.DefaultBack
    )
    viewmodel.ChangeBottomBarState(BottomBarState.NoBottomBar)

    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)

    var user_name  = remember { mutableStateOf("") } ; var username_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var user_surname  = remember { mutableStateOf("") }; var usersurname_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var user_condition  = remember { mutableStateOf("") }; var usercondition_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var kin = remember { mutableStateOf("") }; var kin_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var patient_id = remember { mutableStateOf("") }; var patientid_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var doctorname = remember { mutableStateOf("") }; var doctorname_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var user_wardno  = remember { mutableStateOf("") }; var userwardno_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var phoneno  = remember { mutableStateOf("") }; var phoneno_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var gender = remember { mutableStateOf("") }; var gender_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var age = remember { mutableStateOf("") }; var age_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var iscritcal  = remember { mutableStateOf(false) }
    var user_patientDeparment = remember { mutableStateOf("") } ; var user_patientDeparment_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var AdmissionDate = remember { mutableStateOf(0L) } ; var AdmissionDate_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

    Column( modifier = modifier.fillMaxSize().background(AppBg)
        .verticalScroll( rememberScrollState() ) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(modifier = Modifier.padding(top = ScreenWidth(0.05).dp).fillMaxHeight().fillMaxWidth(0.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            SingupFeilds(
                label = "Firstname",
                textstate = user_name,
                placeholdertext = "Enter patient name",
                username_ststate
            )
            SingupFeilds(
                label = "Surname",
                textstate = user_surname,
                placeholdertext = "Enter patient surname",
                usersurname_ststate
            )
            SingupFeilds(
                label = "Doctor",
                textstate = doctorname,
                placeholdertext = "Enter the assigned Doctor",
                doctorname_ststate
            )
            SingupFeilds(
                label = "Kin",
                textstate = kin,
                placeholdertext = "Enter kin full name",
                kin_ststate
            )
            SingupFeilds(
                label = "Paitent ID",
                textstate = patient_id,
                placeholdertext = "Enter patient id assigned  by your hospital   ",
                patientid_ststate
            )
            SingupFeilds(
                label = "Phone no",
                textstate = phoneno,
                placeholdertext = "Enter patient phone number ",
                phoneno_ststate
            )
            SingupFeilds(
                label = "Conditon",
                textstate = user_condition,
                placeholdertext = "Enter patients diagnosed condition  ",
                doctorname_ststate
            )
            SingupFeilds(label = "Department ",
                textstate = user_patientDeparment,
                placeholdertext = "Enter assigned medical department",
                user_patientDeparment_ststate
            )
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp ),
                 horizontalArrangement = Arrangement.SpaceEvenly ){
                    SignupFeildsSecond(modifier = Modifier.weight(1f), gender , gender_ststate , "Gender" , false)
                    SignupFeildsSecond(modifier = Modifier.weight(1f), user_wardno , userwardno_ststate , "Ward no" , false)
                    SignupFeildsSecond(modifier = Modifier.weight(0.6f), age , age_ststate , "Age" , true)
            }

            Row(modifier = Modifier.fillMaxWidth()  ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Is patient in critical condition and needs to prioritised?" , fontSize = 13.sp , modifier = Modifier.fillMaxWidth(0.85f) , color = Color.DarkGray)
                Switch(checked = iscritcal.value , onCheckedChange = {
                    iscritcal.value = it },
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor = Color.White.copy(alpha = 0.35f),
                        uncheckedTrackColor = Color.Gray,
                        uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                        checkedTrackColor = HTextClr,
                        checkedThumbColor = Color.White,
                        checkedBorderColor = HTextClr
                    )
                )
            }

            val addPatientState by viewmodel.addPatientState.collectAsState()
            var errormessage by remember { mutableStateOf("") }
            var isloading by remember { mutableStateOf(false) }

            when(val state = addPatientState){
                is AddPatientState.AddPatientFailed -> { isloading = false ; errormessage = state.errormsg }
                is AddPatientState.AddingPatient -> { errormessage = "" ; isloading = true }
                is AddPatientState.PatientAdded -> { navcontroller.popBackStack()
                  viewmodel.AddPatientResset()
                }
                else -> Unit
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

                AdmissionDate.value = System.currentTimeMillis()
                fun NotEmptyFeilds() : Boolean {
                    var isvalid = true
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

                    if( user_condition.value.isBlank()){  usercondition_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { usercondition_ststate.value = SupportTextState.ideal }

                    if( user_wardno.value.isBlank()){  userwardno_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { userwardno_ststate.value = SupportTextState.ideal }
                    if(gender.value.isBlank()){gender_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                    else { gender_ststate.value = SupportTextState.ideal }

                    if(age.value.isBlank()){age_ststate.value = SupportTextState.empty("Required") ; isvalid = false}
                    else { age_ststate.value = SupportTextState.ideal }

                    if( user_patientDeparment.value.isBlank()){  user_patientDeparment_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else {  user_patientDeparment_ststate.value = SupportTextState.ideal }

                    if( AdmissionDate.value == 0L){  AdmissionDate_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else { AdmissionDate_ststate.value = SupportTextState.ideal }

                    return isvalid
                }
                if(NotEmptyFeilds()){
                    try { // Store These in ViewModel
                         val patientinfo = PatientInfo(
                            p_firstname =  user_name.value, p_surename = user_surname.value ,
                            p_patientid = patient_id.value , wardno = user_wardno.value,
                            condition = user_condition.value,
                            p_doctor = doctorname.value,
                            p_phoneno = phoneno.value,
                            p_gender = gender.value , p_age = age.value.toInt(),
                            iscritical = iscritcal.value,
                             department = user_patientDeparment.value,
                             admissionDate = AdmissionDate.value
                         )

                        viewmodel.SavePatientInfoFirestore(patientinfo)
                    // Save a Copy of User Entered Patient Info in View Model
                    }catch (e : Exception){
                        errormessage = "Age can only have numbers"
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