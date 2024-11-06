package com.example.nurseflowd1.screens.nurseauth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.domain.AuthState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25
import androidx.compose.runtime.getValue
import com.example.nurseflowd1.screens.Destinations

// NURSE LOGIN
@Composable
fun LoginFields(label : String, textstate : MutableState<String>, ScreenHeight : Int, placeholder : String , supportingText : MutableState<SupportTextState> ){
    // "Nurse Id" Label
    Text(label,
        color = HTextClr,
        modifier = Modifier.padding(bottom = 2.dp, start = 6.dp),
        style = TextStyle(fontFamily = jersery25, fontWeight = FontWeight.Thin, fontSize = (ScreenHeight * 0.03).sp  )
    )
    // Nurse Id OutlinedTextField
    val softwarekeyboard = GiveKeyboard()
    val isError = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = textstate.value,
        onValueChange = { input -> textstate.value = input },
        placeholder = {
            Text(placeholder, color = Color.White.copy(alpha = 0.6f) , fontSize = (ScreenHeight * 0.02).sp)
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth(),
        supportingText = {
            when(val state = supportingText.value){
                is SupportTextState.empty -> {  Text(state.errormsg) ; isError.value = true }
                is SupportTextState.invalid -> { Text(state.errormsg) ; isError.value = true }
                else -> { isError.value = false}
            }
        },
        isError = isError.value ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
               softwarekeyboard.hide()
            }
        )
        // Apply the same height
    )
}


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navcontroller: NavController, viewmodel: AppVM) {
    val ScreenHeight = LocalConfiguration.current.screenHeightDp


    val errormessage = remember { mutableStateOf("") }
    // If users keep entering null fields it will show message or execute statement once but not again and again
    // The same Functionality works with wrong email and password tho , if they keep using wrong creds , it keeps messaging the user with same authstate which is failed
    val authState by viewmodel.authstate.collectAsState()
    LaunchedEffect(authState) {
        when(val state = authState){
            is AuthState.Authenticated ->{ navcontroller.popBackStack( route = Destinations.NurseDboardScreen.ref , inclusive = false) }
            is AuthState.Failed -> {  errormessage.value = state.message }
            is AuthState.LoadingAuth -> { Log.d("TAGY" , "Loging in.......") }
            else -> Unit
        }
    }

    LoginContent(modifier , ScreenHeight , navcontroller , viewmodel , errormessage)
}

@Composable
fun LoginContent(modifier: Modifier,
                 ScreenHeight: Int,
                 navcontroller : NavController,
                 viewmodel: AppVM,
                 errormessage : MutableState<String>
){
    var userin_id = remember { mutableStateOf("") }
    var userin_password = remember { mutableStateOf("") }
    var supportingtext_email : MutableState<SupportTextState> = remember{ mutableStateOf(SupportTextState.ideal) }
    var supportingtext_password : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }


    Column (modifier = modifier.background(AppBg).fillMaxSize().verticalScroll( rememberScrollState() ),
        verticalArrangement =  Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Login Fields
        Column(modifier = Modifier.fillMaxWidth(0.8f)){
            LoginFields( label = "Email Id" , textstate = userin_id , ScreenHeight = ScreenHeight , "Enter your Email Id" , supportingtext_email)
            Spacer(Modifier.size ( (ScreenHeight * 0.015).dp ) )
            LoginFields( label = "Password" , textstate = userin_password , ScreenHeight = ScreenHeight , "Enter your password" , supportingtext_password)
        }
        // Login / Signup Button

        Column( modifier = Modifier.fillMaxWidth(0.8f), horizontalAlignment = Alignment.CenterHorizontally){
            if (errormessage.value.isNotEmpty()) {
                Text( text = errormessage.value,
                    style = TextStyle(fontSize = (ScreenHeight * 0.018).sp),
                    color = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.width( ( ScreenHeight * 0.3).dp )
                )
            }
            // Go to Nurse Dashboard page
            Button( onClick = {
                if(userin_id.value.isBlank() ){
                    supportingtext_email.value = SupportTextState.empty("email can be empty")
                }
                else if( userin_password.value.isBlank()) {
                    supportingtext_password.value = SupportTextState.empty("password cannot be empty")
                }else {
                    supportingtext_email.value = SupportTextState.ideal
                    supportingtext_password.value = SupportTextState.ideal
                    viewmodel.LoginUser(userin_id.value, userin_password.value)
                }
            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = (ScreenHeight * 0.02).dp )
                    .size(width = (ScreenHeight * 0.15).dp , height = (ScreenHeight * 0.05) .dp),
                contentPadding = PaddingValues(0.dp)
            ){
                Text("Login" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = (ScreenHeight * 0.03).sp)
            }


            // Signup Row
            Row(verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Absolute.Center ,  modifier = Modifier.fillMaxWidth().padding(top = (ScreenHeight * 0.01).dp)
            ){

                Text( "Have not singed up at NurseFlow yet?" , color = Color.White ,
                    softWrap = true,
                    modifier = Modifier.padding( end = (ScreenHeight * 0.001).dp )
                        .width( (ScreenHeight * 0.2).dp ),
                    fontSize = (ScreenHeight * 0.02).sp,
                    lineHeight = (ScreenHeight * 0.02).sp
                )
                Button( onClick = {
                    navcontroller.navigate(Destinations.RegisScreen.ref)
                }, colors = ButtonColors( containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Black,
                    disabledContainerColor = Color.White ),
                    modifier = Modifier.size(width = (ScreenHeight * 0.14).dp , height = (ScreenHeight * 0.05).dp),
                    contentPadding = PaddingValues(0.dp)
                ){
                    Text("Signup" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = (ScreenHeight * 0.02).sp )
                }

            }

        }

    }
}






