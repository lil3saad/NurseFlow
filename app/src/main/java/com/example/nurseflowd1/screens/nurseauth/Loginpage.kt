package com.example.nurseflowd1.screens.nurseauth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.AuthState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont
import androidx.compose.runtime.getValue
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.TopAppBarState
import com.google.android.play.integrity.internal.k

// NURSE LOGIN
@Composable
fun LoginFields(label : String, textstate : MutableState<String>, placeholder : String , supportingText : MutableState<SupportTextState> ){

    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)

    // "Nurse Id" Label
    Text(label,
        color = HTextClr,
        modifier = Modifier.padding(bottom = 2.dp, start = 6.dp),
        style = TextStyle(fontFamily = Headingfont, fontWeight = FontWeight.Thin, fontSize = ScreenWidth(0.07).sp  )
    )
    // Nurse Id OutlinedTextField
    val softwarekeyboard = giveKeyboard()
    val isError = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = textstate.value,
        onValueChange = { input -> textstate.value = input },
        placeholder = {
            Text(placeholder, color = Color.White.copy(alpha = 0.6f) , fontSize = ScreenWidth(0.055).sp )
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth(),
        supportingText = {
            when(val state = supportingText.value){
                is SupportTextState.empty -> {  Text(state.errormsg) ; isError.value = true }
                is SupportTextState.ideal -> { isError.value = false}
                else -> Unit
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
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.White,
            focusedBorderColor = HTextClr.copy(alpha = 0.75f)
        )
        // Apply the same height
    )
}


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navcontroller: NavController, viewmodel: AppVM) {


    viewmodel.SetTopBarState(TopAppBarState.AppNameBar)
    // If users keep entering null fields it will show message or execute statement once but not again and again
    // The same Functionality works with wrong email and password tho , if they keep using wrong creds , it keeps messaging the user with same authstate which is failed
    val authState by viewmodel.authstate.collectAsState()

    val errormessage = remember { mutableStateOf("") }
    val isloading = remember { mutableStateOf(false) }
        when(val state = authState){
            is AuthState.Authenticated ->{
                navcontroller.popBackStack( route = Destinations.NurseDboardScreen.ref , inclusive = false)
            }
            is AuthState.Failed -> {  errormessage.value = state.message ; isloading.value = false

            }
            is AuthState.LoadingAuth -> { errormessage.value = ""; isloading.value = true
                Log.d("TAGY" , "Loging in.......")
            }
            else -> Unit
        }
    LoginContent(modifier , navcontroller , viewmodel , errormessage , isloading)

}

@Composable
fun LoginContent(modifier: Modifier,
                 navcontroller : NavController,
                 viewmodel: AppVM,
                 errormessage : MutableState<String>,
                 isloading : MutableState<Boolean>
){
    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)


    var userin_id = remember { mutableStateOf("") } ; var supportingtext_email : MutableState<SupportTextState> = remember{ mutableStateOf(SupportTextState.ideal) }
    var userin_password = remember { mutableStateOf("") } ; var supportingtext_password : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }


    Column (modifier = modifier.background(AppBg).fillMaxSize().verticalScroll( rememberScrollState() ),
        verticalArrangement =  Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Login Fields
        Column(modifier = Modifier.fillMaxWidth(0.8f)){
            LoginFields( label = "Email Id" , textstate = userin_id  , "Enter your Email Id" , supportingtext_email)
            Spacer(Modifier.size ( ScreenWidth(0.025).dp ) )
            LoginFields( label = "Password" , textstate = userin_password ,  "Enter your password" , supportingtext_password)
        }
        // Login / Signup Button

        Column( modifier = Modifier.fillMaxWidth(0.8f), horizontalAlignment = Alignment.CenterHorizontally){
            if (errormessage.value.isNotBlank()) {
                Text( text = errormessage.value,
                    style = TextStyle(fontSize = ScreenWidth(0.07).sp ),
                    color = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.width( ScreenWidth(0.1).dp )
                )
            }
            if(isloading.value){
                CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
            }
            // Go to Nurse Dashboard page
            Button( onClick = {
                fun FieldEmpty() : Boolean {
                    var isempty = false
                    if(userin_id.value.isBlank() ){
                        supportingtext_email.value = SupportTextState.empty("email can be empty") ; isempty = true
                    }else supportingtext_email.value = SupportTextState.ideal
                    if( userin_password.value.isBlank()) {
                        supportingtext_password.value = SupportTextState.empty("password cannot be empty") ; isempty = true
                    }else supportingtext_password.value = SupportTextState.ideal

                    return isempty
                }
                if (!FieldEmpty()){
                    supportingtext_email.value = SupportTextState.ideal
                    supportingtext_password.value = SupportTextState.ideal
                    viewmodel.LoginUser(userin_id.value, userin_password.value)
                }
            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidth(0.05).dp )
                    .size(width = ScreenWidth(0.3).dp  , height =  ScreenWidth(0.1).dp  ),
                contentPadding = PaddingValues(0.dp)
            ){
                Text("Login" , fontFamily = Headingfont , fontWeight = FontWeight.Bold , fontSize = ScreenWidth(0.07).sp)
            }


            // Signup Row
            Row(verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.Absolute.Center ,
                modifier = Modifier.fillMaxWidth().padding(top =ScreenWidth(0.1).dp )
            ){

                Text( "Have not singed up at NurseFlow yet?" , color = Color.White ,
                    softWrap = true,
                    modifier = Modifier
                        .padding( end = ScreenWidth(0.02).dp )
                        .width( ScreenWidth(0.5).dp )
                        .wrapContentWidth(),
                    fontSize = ScreenWidth(0.045).sp ,
                    lineHeight = ScreenWidth(0.06).sp
                )
                Button( onClick = {
                    navcontroller.navigate(Destinations.RegisScreen.ref)
                }, colors = ButtonColors( containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Black,
                    disabledContainerColor = Color.White ),
                    modifier = Modifier.size(width = ScreenWidth(0.25).dp  , height = ScreenWidth(0.1).dp  ),
                    contentPadding = PaddingValues(0.dp)
                ){
                    Text("Signup" , fontFamily = Headingfont , fontWeight = FontWeight.Bold , fontSize = ScreenWidth(0.055).sp )
                }

            }

        }

    }
}






