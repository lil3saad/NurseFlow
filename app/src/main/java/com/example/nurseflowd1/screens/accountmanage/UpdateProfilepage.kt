package com.example.nurseflowd1.screens.accountmanage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.nurseflowd1.datamodels.NurseInfo
import com.example.nurseflowd1.screens.nurseauth.SignupFeildsSecond
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.screens.nurseauth.SupportTextState.*
import com.example.nurseflowd1.screens.nurseauth.screenHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UpdateProfilePage(modifier: Modifier , navController: NavController , viewmodel : AppVM){

    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("NurseFlow"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.DefaultBack
    )
    viewmodel.ChangeBottomBarState(BottomBarState.NoBottomBar)
    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)
    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center){


        val nurseprofilestate by viewmodel.nurseprofilestate.collectAsState()
        var fetchednurseinfo by remember { mutableStateOf(NurseInfo()) }

        when (val state = nurseprofilestate) {
            is NurseProfileState.Failed -> { Log.d("TAGY", "FETCHING NURSE PROFILE PLEASE WAIT ${state.errormsg}") }
            is NurseProfileState.Fetched -> { fetchednurseinfo = state.nurse // Update the state when fetched
                Column(modifier = Modifier.padding(top = ScreenWidth(0.1).dp )
                    .fillMaxWidth(0.9f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    var user_name = remember { mutableStateOf(fetchednurseinfo.N_name) }
                    var username_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var user_surname = remember { mutableStateOf(fetchednurseinfo.N_surname) }
                    var usersurname_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var hospital_name = remember { mutableStateOf(fetchednurseinfo.N_hospitalname) }
                    var hospitalname_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var hospital_id = remember { mutableStateOf(fetchednurseinfo.N_hospitalid) } // Updated hospital_id to be from fetchednurseinfo
                    var hospitalid_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var Council = remember { mutableStateOf(fetchednurseinfo.N_council) } // Updated Council to be from fetchednurseinfo
                    var council_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var nurse_license_id = remember { mutableStateOf(fetchednurseinfo.N_registrationid) } // Updated nurse_license_id to be from fetchednurseinfo
                    var nurselicense_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var gender = remember { mutableStateOf(fetchednurseinfo.N_gender) } // Updated gender to be from fetchednurseinfo
                    var gender_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    var age = remember { mutableStateOf(fetchednurseinfo.N_age.toString()) } // Updated age to be from fetchednurseinfo
                    var age_ststate: MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

                    SingupFeilds(
                        label = "FirstName",
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
                        label = "State Council",
                        textstate = Council,
                        placeholdertext = "Enter name of your nursing council",
                        council_ststate
                    )
                    SingupFeilds(
                        label = "Registration Id",
                        textstate = nurse_license_id,
                        placeholdertext = "Enter Nurse Registration Number or Id issued by your state council",
                        nurselicense_ststate
                    )


                    Row(modifier = Modifier.fillMaxWidth().padding( horizontal = ScreenWidth(0.2).dp)){
                        SignupFeildsSecond(modifier = Modifier.weight(1f), gender , gender_ststate , "Gender" , false)
                        Spacer( modifier = Modifier.size(ScreenWidth(0.1).dp))
                        SignupFeildsSecond(modifier = Modifier.weight(0.6f), age , age_ststate , "Age", true)
                    }

                    var errormessage by remember { mutableStateOf("") }
                    if(errormessage.isNotBlank()){
                        Text(errormessage, style = TextStyle( color = Color.Red.copy(alpha = 0.8f) , fontSize = screenHeight(0.02).sp))
                    }
                    Button( onClick = {
                        fun NotEmptyFeilds() : Boolean { var isvalid = true
                            if( user_name.value.isBlank()){ username_ststate.value =
                                empty("Required"); isvalid = false}
                            else { username_ststate.value = SupportTextState.ideal }

                            if( user_surname.value.isBlank()){ usersurname_ststate.value =
                                empty("Required"); isvalid = false}
                            else { usersurname_ststate.value = SupportTextState.ideal }

                            if( hospital_name.value.isBlank()){ hospitalname_ststate.value =
                                empty("Required"); isvalid = false}
                            else { hospitalname_ststate.value = SupportTextState.ideal }

                            if( hospital_id.value.isBlank()){ hospitalid_ststate.value =
                                empty("Required"); isvalid = false}
                            else {hospitalid_ststate.value = SupportTextState.ideal }

                            if(Council.value.isBlank()){ council_ststate.value = empty("Required"); isvalid = false }
                            else { council_ststate.value = SupportTextState.ideal }

                            if( nurse_license_id.value.isBlank()){ nurselicense_ststate.value =
                                empty("Required"); isvalid = false}
                            else { nurselicense_ststate.value = SupportTextState.ideal }

                            if(gender.value.isBlank()){gender_ststate.value = empty("Required"); isvalid = false}
                            else { gender_ststate.value = SupportTextState.ideal }

                            if(age.value.isBlank()){age_ststate.value = empty("Required"); isvalid = false}
                            else { age_ststate.value = SupportTextState.ideal }

                            return isvalid
                        }
                        if (NotEmptyFeilds()){
                            try {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val nurseinfo = NurseInfo( N_name = user_name.value , N_surname =  user_surname.value ,
                                        N_hospitalname =  hospital_name.value, N_hospitalid = hospital_id.value , N_council = Council.value,
                                        N_registrationid = nurse_license_id.value , N_gender = gender.value , N_age = age.value.toInt() ,
                                        uid = fetchednurseinfo.uid ,
                                        profilepicid = viewmodel.getProfilePicID()
                                    )
                                    viewmodel.SaveNurseInfoInVm(nurseinfo)
                                    viewmodel.UpdateNurseProfile()
                                }
                            }catch (e : Exception){
                                errormessage = "Age can only have numbers"
                                Log.d("TAGY" , "Error ${e.cause} $e ${e.message} !UserRegisPage")
                            }
                        }
                    },
                        colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                        modifier = Modifier.padding(top = ScreenWidth (0.04).dp ).size(width = ScreenWidth(0.50).dp , height = ScreenWidth(0.11) .dp)
                    ) {
                        Text("Update" , fontFamily = Headingfont , fontWeight = FontWeight.Bold , fontSize = ScreenWidth (0.05).sp)
                    }
                }

            }
            is NurseProfileState.Loading -> {
                Box(modifier = modifier.fillMaxSize().background(AppBg), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp) }
                Log.d("TAGY" , "LOADING...")

            }
            NurseProfileState.UpdateDone -> Unit

        }

    }
}