package com.example.nurseflowd1.screens.accountmanage

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.R
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.jersery25
import androidx.compose.runtime.getValue
import com.example.nurseflowd1.datamodels.NurseInfo
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import com.example.nurseflowd1.domain.ProfilePictureState


@Composable
fun AccountScreen( modifier : Modifier = Modifier, viewmodel : AppVM , navcontroller: NavController){


    @Composable
    fun ScreenHeight(k : Double) : Double = LocalConfiguration.current.screenHeightDp * k
    @Composable
    fun DisplayTextFeild(text : String , label : String){
        Text( text = text , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.03).sp),
            modifier = Modifier
                .border(
                    0.5.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
                .height(ScreenHeight(0.05).dp)
                .padding(ScreenHeight(0.013).dp)
        )
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            Text( text = label , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.02).sp), textAlign = TextAlign.End,
                modifier = Modifier.width(ScreenHeight(0.30).dp)
            )
        }

    }

    Column( modifier = modifier.fillMaxSize().background(AppBg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        val nurseprofilestate by viewmodel.nurseprofilestate.collectAsState()
        viewmodel.FetchNurseProfile()
        var fetchenurseinfo = NurseInfo()
        when(val state = nurseprofilestate) { // How does When State Automatically cast the NurseInfoState to Corresponding type even the variable is private
                is NurseProfileState.Failed -> { Log.d("TAGY" , "Error During Fetch  ${state.errormsg}") }
                is NurseProfileState.Fetched -> { fetchenurseinfo = state.nurse }
                is NurseProfileState.Loading -> {
                    Box(modifier = modifier
                        .fillMaxSize()
                        .background(AppBg), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
                    }
                }
        }


        val photopicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { pickeduri ->
                if (pickeduri != null) {
                          viewmodel.saveProfileUri(pickeduri)
                    Log.d("PhotoPicker", "PHOTO PICKED $pickeduri")
                } else Log.d("PhotoPicker", "No media selected")
        }


        val profilepicstate by viewmodel.profilepicstate.collectAsState()
        Column(modifier = Modifier.fillMaxWidth() ,
            horizontalAlignment = Alignment.CenterHorizontally
        ){ // How to give the User Crop the Images after picking it
            Image(
                painter =  when(val state = profilepicstate){
                    ProfilePictureState.Empty -> { painterResource(R.drawable.profilepic)}
                    is ProfilePictureState.Picture -> { rememberAsyncImagePainter(state.uri) }
                },
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
                    .clip(CircleShape)
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier .clickable{
                    photopicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Text("Change Profile" ,
                    modifier = Modifier.padding(top = ScreenHeight(0.01).dp)
                )
                Icon( imageVector = Icons.Default.Add , contentDescription =  "Edit Profile",
                    modifier = Modifier.padding(top = 12.dp)
                        .size(25.dp)

                )
            }

        }

        // Profile
        Column(modifier = Modifier.fillMaxWidth(0.9f),
        ){
            Text( text = "Name" , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.02).sp , color = HTextClr) , modifier = Modifier.padding(start = 12.dp, bottom = 4.dp))
            DisplayTextFeild(text = "${fetchenurseinfo.N_name} ${fetchenurseinfo.N_surname}" , "")

            Text( text = "Hospital Id " , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.02).sp , color = HTextClr) , modifier = Modifier.padding(start = 12.dp , bottom = 4.dp)  )
            DisplayTextFeild(text = fetchenurseinfo.N_hospitalid, label = fetchenurseinfo.N_hospitalname)

            Text( text = "Registration Id" , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.02).sp , color = HTextClr) , modifier = Modifier.padding(start = 12.dp, bottom = 4.dp))
            DisplayTextFeild(text = fetchenurseinfo.N_registrationid, label = fetchenurseinfo.N_council)

            Row(modifier = Modifier
                .padding(start = 12.dp, top = 18.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(25.dp)){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text( text = "Gender : " , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.02).sp) , color = HTextClr)
                    Text( text = fetchenurseinfo.N_gender , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.03).sp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text( text = "Age : " , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.02).sp) , color = HTextClr)
                    Text( text = fetchenurseinfo.N_age.toString() , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.03).sp))
                }
            }
        }

        // Drop Down Menu
        // Menu Options
        Column( modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = ScreenHeight(0.05).dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)){

            Text( text = "Edit Profile" , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.03).sp , color = HTextClr),
                textDecoration = TextDecoration.Underline ,
                modifier = Modifier.clickable{
                    navcontroller.navigate( route = Destinations.UpdateProfileScreen.ref)
                }
            )
            Text( text = "Settings" , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.03).sp , color = HTextClr),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable{
                    navcontroller.navigate( route = Destinations.AccSettingsScreen.ref)
                }
            )
            Text( text = "Logout" , style = TextStyle(fontFamily = jersery25, fontSize = ScreenHeight(0.03).sp , color = HTextClr),textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable{
                    // Throw a Confirmation Alert Box
                    viewmodel.SingOut()
                    navcontroller.popBackStack(route = Destinations.NurseDboardScreen.ref , inclusive = false)
                }
            )
        }
    }
}

