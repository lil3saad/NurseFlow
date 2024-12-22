package com.example.nurseflowd1.screens.accountmanage

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.nurseflowd1.ui.theme.Headingfont
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.nurseflowd1.datamodels.NurseInfo
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.ui.theme.Bodyfont
import kotlinx.coroutines.launch


@Composable
fun AccountScreen( modifier : Modifier = Modifier, viewmodel : AppVM , navcontroller: NavController){

    // Change State of TopBarState
    viewmodel.ChangeTopBarState( barstate = AppBarTitleState.DisplayTitle("Profile"),
        colorState = AppBarColorState.DefaultColors,
        iconState = NavigationIconState.DefaultBack
    )
    viewmodel.ChangeBottomBarState(BottomBarState.AccountPage)


    @Composable
    fun ScreenWidth(k : Double) : Double = LocalConfiguration.current.screenWidthDp * k

    @Composable
    fun DisplayTextFeild(text : String , label : String){
        Text( text = text , style = TextStyle(fontFamily = Bodyfont, fontSize = 16.sp ) ,
            modifier = Modifier
                .border(0.5.dp, color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
                .height(46.dp)
                .padding(12.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            Text( text = label , style = TextStyle(fontFamily = Bodyfont, fontSize = 16.sp ), textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }



    Column(modifier = Modifier.background(AppBg)
        .fillMaxSize()
        .verticalScroll( rememberScrollState() ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        Column( modifier = modifier.fillMaxWidth().background(AppBg).padding(top = ScreenWidth(0.1).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            var fetchenurseinfo = NurseInfo()
            val nurseprofilestate by viewmodel.nurseprofilestate.collectAsState()

            LaunchedEffect(nurseprofilestate) { viewmodel.FetchNurseProfile() ; viewmodel.getProfilePicState() }

            when(val state = nurseprofilestate) {
                // How does When State Automatically cast the NurseInfoState to Corresponding type even the variable is private
                is NurseProfileState.Failed -> { Log.d("TAGY" , "Error During Fetch  ${state.errormsg}") }
                is NurseProfileState.Fetched -> { fetchenurseinfo = state.nurse }
                is NurseProfileState.Loading -> {
                    Box(modifier = modifier.fillMaxSize().background(AppBg),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
                    }
                }
                is NurseProfileState.UpdateDone -> {
                    viewmodel.FetchNurseProfile()
                }
            }

            val context = LocalContext.current
            val photopicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                    pickeduri ->
                if (pickeduri != null) {
                    viewmodel.saveProfileUri(pickeduri , context = context)
                    Log.d("PhotoPicker", "PHOTO PICKED $pickeduri")
                } else Log.d("PhotoPicker", "No media selected")
            }



            Column(modifier = Modifier.fillMaxWidth() ,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                val profilepicstate by viewmodel.profilepiucstate.collectAsState()

                when(val state = profilepicstate){
                    ProfilePictureState.default -> {
                        Image(imageVector = ImageVector.vectorResource(R.drawable.profilepicture),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(200.dp)
                            .clip(CircleShape))
                    } // Let ProfileImage Stay Default
                    ProfilePictureState.Added -> viewmodel.getProfilePicState()
                    is ProfilePictureState.Fetched -> {
                        Image(
                            bitmap =  state.image.asImageBitmap(),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(200.dp)
                                .clip(CircleShape)
                        )
                    }
                    is ProfilePictureState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(150.dp),
                            color = HTextClr
                        )
                    }
                    else -> {
                        Image(imageVector = ImageVector.vectorResource(R.drawable.profilepicture),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(200.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                        modifier =   Modifier.padding(top = 6.dp)
                            .clickable{
                            photopicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ){
                        Text("Change Profile Picture" , modifier = Modifier.padding( top = 6.dp) , fontSize = 14.sp
                        )
                        Icon( imageVector = Icons.Default.Edit , contentDescription =  "Edit Profile",
                            modifier = Modifier.padding(start = 3.dp )
                                .size(25.dp)
                        )
                }

            }

            // Profile
            Column(modifier = Modifier.fillMaxWidth(0.9f),
            ){
                Text( text = "Name" , style = TextStyle(fontFamily = Headingfont, fontSize = 17.sp , color = HTextClr),
                    modifier = Modifier.padding(start = 12.dp, bottom = 4.dp) )
                DisplayTextFeild(text = "${fetchenurseinfo.N_name} ${fetchenurseinfo.N_surname}" , "")

                Text( text = "Hospital Id " , style = TextStyle(fontFamily = Headingfont, fontSize = 17.sp , color = HTextClr) , modifier = Modifier.padding(start = 12.dp , bottom = 4.dp)  )
                DisplayTextFeild(text = fetchenurseinfo.N_hospitalid, label = fetchenurseinfo.N_hospitalname)

                Text( text = "Registration Id" , style = TextStyle(fontFamily = Headingfont, fontSize =  17.sp , color = HTextClr) , modifier = Modifier.padding(start = 12.dp, bottom = 4.dp))
                DisplayTextFeild(text = fetchenurseinfo.N_registrationid, label = fetchenurseinfo.N_council)

                Row(modifier = Modifier.padding(start = 12.dp, top = 18.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text( text = "Gender : " , style = TextStyle(fontFamily = Headingfont, fontSize = 16.sp , color = HTextClr))
                        Text( text = fetchenurseinfo.N_gender , style = TextStyle(fontFamily = Headingfont, fontSize = 16.sp ))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text( text = "Age : " , style = TextStyle(fontFamily = Headingfont, fontSize = 16.sp , color = HTextClr))
                        Text( text = fetchenurseinfo.N_age.toString() , style = TextStyle(fontFamily = Bodyfont, fontSize = 16.sp ))
                    }
                }
            }

            // Drop Down Menu
            // Menu Options
            Column( modifier = Modifier.fillMaxWidth(0.9f)
                .padding(top = ScreenWidth(0.05).dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text( text = "Settings" , style = TextStyle(fontFamily = Headingfont, fontSize = 17.sp , color = HTextClr),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable{
                        navcontroller.navigate( route = Destinations.AccSettingsScreen.ref)
                    }
                )
                Text( text = "Logout" , style = TextStyle(fontFamily = Headingfont, fontSize = 17.sp , color = HTextClr),textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable{
                        // Throw a Confirmation Alert Box
                        viewmodel.SingOut()
                        navcontroller.popBackStack(route = Destinations.NurseDboardScreen.ref , inclusive = false)
                    }
                )
            }
        }
    }
}