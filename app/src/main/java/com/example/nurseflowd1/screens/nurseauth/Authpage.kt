package com.example.nurseflowd1.screens.nurseauth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.domain.AuthState
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25


@Composable
fun GiveKeyboard() : SoftwareKeyboardController =  LocalSoftwareKeyboardController.current!!

@Composable
fun SingupFeilds(label: String, textstate: MutableState<String>, placeholdertext: String , supportextstate : MutableState<SupportTextState>){
    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)
    Column(modifier = Modifier.padding(bottom = ScreenWidth(0.05).dp)){
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ){
            Text(
                label, color = HTextClr , style = TextStyle(
                fontSize = ScreenWidth(0.05).sp ,
                fontFamily = jersery25
            ))
        }
        val softwarekeyboard = GiveKeyboard()
        val iserror = remember { mutableStateOf(false) }
        TextField(
            value = textstate.value , onValueChange = {
                    usertext -> textstate.value = usertext
            } ,
            placeholder = {
                Text(placeholdertext, color = Color.White.copy(0.50f))
            },
            colors = TextFieldDefaults.colors( unfocusedContainerColor = AppBg , unfocusedIndicatorColor = Color.White.copy(0.50f) ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                       softwarekeyboard.hide()
                }
            ),
            supportingText = {
                when(val state = supportextstate.value){
                    is SupportTextState.empty -> { Text(state.errormsg) ; iserror.value = true}
                    is SupportTextState.ideal -> { iserror.value = false}
                    else -> Unit
                }
            },
            isError = iserror.value
        )
    }
}

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavController , viewmodel : AppVM
) {
    @Composable
    fun ScreenWidth(k : Double ) : Double = (LocalConfiguration.current.screenWidthDp * k)
    val context = LocalContext.current

    var user_email = remember { mutableStateOf("") } ; var useremail_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var password1 = remember { mutableStateOf("") } ; var password1_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
    var password2 = remember { mutableStateOf("") }; var password2_ststate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }

    val ErrorMessage = remember { mutableStateOf("") }
    val authState = viewmodel.authstate.collectAsState()
    // Handling the click event of login button
    // LaunchEffect observes the side effects when the key provided changes and makes sure the
    // execution after the key changes happens only once
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> {
                viewmodel.CreateNurseProfile()
                navController.popBackStack( route = Destinations.NurseDboardScreen.ref , inclusive = false)
            }
            is AuthState.Failed -> { ErrorMessage.value = (authState.value as AuthState.Failed).message }
            is AuthState.LoadingAuth -> { Log.d("TAGY" , "Creating user please wait...") }
            else -> Unit
        }
    }
    Column( modifier = modifier.fillMaxSize().background(AppBg).verticalScroll( rememberScrollState() ),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Welcome to NurseFlow " , style = TextStyle(
                fontSize = (ScreenWidth(0.08).sp) , fontFamily = jersery25 , color = HTextClr),
                modifier = Modifier.padding(bottom = ScreenWidth(0.1).dp)
            )
            SingupFeilds("UserId" , user_email , placeholdertext = "Enter email id or phone number", useremail_ststate)
            SingupFeilds("Password" , password1 , placeholdertext = "Set Passowrd" , password1_ststate)
            SingupFeilds("Confirm " , password2, placeholdertext = "Re-enter password" , password2_ststate)
            // When the User Closes the App without Registering , he still gets redirected to the dashboard because the state becomes authenticated on the auth page itself and not after registrationg
            if(!ErrorMessage.value.isBlank()){
                Text(ErrorMessage.value, style = TextStyle( color = Color.Red.copy(alpha = 0.8f) , fontSize = ScreenWidth(0.04).sp))
            }
            Button( onClick = {
                fun NotEmptyFeilds() : Boolean { var isvalid = true
                    if( user_email.value.isBlank()){ useremail_ststate.value = SupportTextState.empty("Required*") ; isvalid = false}
                    else useremail_ststate.value = SupportTextState.ideal
                    if( password1.value.isBlank()){ password1_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else password1_ststate.value = SupportTextState.ideal
                    if( password2.value.isBlank()){ password2_ststate.value = SupportTextState.empty("Required*")  ; isvalid = false}
                    else password2_ststate.value = SupportTextState.ideal
                    return isvalid
                }
                if(NotEmptyFeilds()){
                    if(  password1.value == password2.value){
                        viewmodel.CreateUser(user_email.value , password2.value)
                    }else {
                        password1_ststate.value = SupportTextState.empty("Passwords Does Not  Match")
                        password2_ststate.value = SupportTextState.empty("Password Does Not Match")
                    }
                }

            },
                colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                modifier = Modifier.padding(top = ScreenWidth (0.04).dp ).size(width = ScreenWidth(0.30).dp , height = ScreenWidth(0.11) .dp)
            ) {
                Text("SignUp" , fontFamily = jersery25 , fontWeight = FontWeight.Bold , fontSize = ScreenWidth (0.05).sp)
            }
        }

    }




}

