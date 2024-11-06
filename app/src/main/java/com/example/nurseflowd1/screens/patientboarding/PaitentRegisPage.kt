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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.screens.Destinations



@Composable
fun Paitent_Regis_Screen( modifier: Modifier = Modifier , navcontroller : NavController , viewmodel: AppVM){
    @Composable
    fun ScreenWidhth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)

    var user_name  = remember { mutableStateOf("") }
    var user_surname  = remember { mutableStateOf("") }
    var phoneno  = remember { mutableStateOf("") }
    var kin = remember { mutableStateOf("") }
    var patient_id = remember { mutableStateOf("") }
    var doctorname = remember { mutableStateOf("") }
    var gender = remember { mutableStateOf("") }
    var age = remember { mutableStateOf("") }

    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll( rememberScrollState() ) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){


        Column(modifier = Modifier
            .fillMaxWidth(0.9f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            SingupFeilds(
                label = "Name",
                textstate = user_name,
                placeholdertext = "Enter paitent name"
            )
            SingupFeilds(
                label = "Surname",
                textstate = user_surname,
                placeholdertext = "Enter patient surname"
            )
            SingupFeilds(
                label = "Phone no",
                textstate = phoneno,
                placeholdertext = "Enter patient phone number "
            )
            SingupFeilds(
                label = "Kin",
                textstate = kin,
                placeholdertext = "Enter kin full name "
            )
            SingupFeilds(
                label = "Paitent ID",
                textstate = patient_id,
                placeholdertext = "Enter paitent id assinged by your hosptial  "
            )
            SingupFeilds(
                label = "Doctor",
                textstate = doctorname,
                placeholdertext = "Enter the assgined Doctor"
            )

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedTextField( value = gender.value , onValueChange = {
                        it -> gender.value = it
                },
                    placeholder = { Text("Gender" , color = Color.White.copy(0.50f) )},
                    modifier = Modifier.fillMaxWidth(0.45f)
                )
                Spacer(modifier = Modifier.size( ScreenWidhth(0.02).dp ))
                OutlinedTextField( value = age.value , onValueChange = {
                        it -> age.value = it
                },
                    placeholder = { Text("Age" , color = Color.White.copy(0.50f) )},
                    modifier = Modifier.fillMaxWidth(0.45f)
                )
            }
            Button( onClick = {
                val patientinfo = PatientInfo(
                    p_name =  user_name.value, p_surename = user_surname.value , p_phoneno = phoneno.value,
                    p_patientid = patient_id.value ,
                    p_doctor = doctorname.value , p_age = age.value , p_gender = gender.value
                )
                    viewmodel.SavePatientInfoFirestore(patientinfo)
                    navcontroller.popBackStack(Destinations.NurseDboardScreen.ref , inclusive = false)

            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidhth (0.04).dp ).size(width = ScreenWidhth(0.50).dp , height = ScreenWidhth(0.11) .dp)
            ){
                Text("Continue -->" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = ScreenWidhth (0.05).sp)
            }
        }


    }
}