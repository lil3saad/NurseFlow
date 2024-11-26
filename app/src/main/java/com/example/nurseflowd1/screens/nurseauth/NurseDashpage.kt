package com.example.nurseflowd1.screens.nurseauth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nurseflowd1.R
import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.AuthState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.TopAppBarState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont
import com.example.nurseflowd1.ui.theme.SecClr
import com.example.nurseflowd1.ui.theme.bottombar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


// NURSE DASHBOARD
// if a Authenticated user coming from the login screen (AFTER NORMAL LOGIN ) is Authenticated and sent to N_Dashboard and the user keeps pressing back the user will be sent back to the login page , not for the first time , not for second time but he will be surely sent back
@Composable
fun NurseDashBoardScreen(modifier: Modifier , navController: NavController , viewmodel : AppVM) {
    val ScreenHeight = LocalConfiguration.current.screenHeightDp  ; val context = LocalContext.current

    viewmodel.SetTopBarState(TopAppBarState.NurseDashBoard)
     viewmodel.SetBottomBarState(BottomBarState.NurseDashBoard)
    //Authentication State

    viewmodel.authStatus() // For AutoLog
    val authState by viewmodel.authstate.collectAsState() // Observing the AuthState
    val gotnursedocid by viewmodel.NurseDocId.collectAsState() // Get Nurse DocumentId by Uid


    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Idle -> { navController.navigate(Destinations.LoginScreen.ref) }
            is AuthState.UnAuthenticated -> { navController.navigate(Destinations.LoginScreen.ref) }
            is AuthState.Failed -> { Toast.makeText(context, (authState as AuthState.Failed).message, Toast.LENGTH_LONG).show() }
            is AuthState.Authenticated ->  {
                Log.d("TAGY" , "HOW IS THIS NURSE AUTHENTICATED")
                if (gotnursedocid is NurseDocIdState.CurrentNurseId) Unit
                else viewmodel.GetNurseDocId()
            }
            else -> Unit
        }
    }

    //PatientList Stat
    val patientliststate by viewmodel.paitientinfolist.collectAsState()
    LaunchedEffect(gotnursedocid) {
        if(gotnursedocid is NurseDocIdState.CurrentNurseId) {
            viewmodel.FetchP_InfoList()
        }
    }
     // Just Get NurseId Once & Fetch paitents everytime NDASH Is Launched but only do this once
    Column(modifier = modifier.fillMaxSize().background(AppBg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){

        TopPanel()
        LazyColumn(modifier = Modifier.padding(top = (ScreenHeight * 0.02).dp).fillMaxWidth(0.9f).fillMaxHeight(0.84f)) {
            when (patientliststate) {
                is PatientListState.emptylist ->{ item { Text("No patients available. Please add patients.") } }
                is PatientListState.PatientsReceived -> {
                    val patientList = (patientliststate as PatientListState.PatientsReceived).patientlist
                    items(patientList) { patient ->
                        val cardPatient = CardPatient(
                            name = "${patient.p_name} ${patient.p_surename}",
                            gender = patient.p_gender,
                            age = patient.p_age.toString(),
                            conditon = patient.p_doctor,
                        )
                        PaitentCard(cardPatient)
                    }
                }
                is  PatientListState.Loadinglist -> { item{
                        Box(modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
                        }
                    }
                }
                else -> Unit
            }
        }

        val barstate by viewmodel.topappbarstate.collectAsState()
        BottomNavBar(navController , barstate )
    }
}

@Composable
fun TopPanel() {

    val ScreenHeight = LocalConfiguration.current.screenHeightDp  ;
    val toppanelshape = RoundedCornerShape(bottomEnd = 45.dp , bottomStart = 45.dp)
    Column(modifier = Modifier.fillMaxWidth().fillMaxWidth(0.5f).background(HTextClr , shape = toppanelshape).padding(vertical = 10.dp, horizontal = 20.dp)
    ){

        Card( colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(0.09f)
        )){
            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)
            ){
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total paitents" , fontSize = 30.sp , fontFamily = Bodyfont)
                    Icon( imageVector = Icons.Default.Search , contentDescription = "Search Patiens" , modifier = Modifier.size((ScreenHeight * 0.04).dp ))
                }
                Row(modifier = Modifier.fillMaxWidth().padding(start = 22.dp)
                ){
                    Text("45" , fontSize = 75.sp , fontFamily = Headingfont)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp) ,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically){
            Button( onClick = {},
                colors = ButtonColors(
                    containerColor = Color.Red.copy(alpha = 0.22f),
                    contentColor = Color.Black ,
                    disabledContentColor = Color.White.copy(alpha = 0.22f),
                    disabledContainerColor = Color.White.copy(alpha = 0.22f)
                ),
                contentPadding = PaddingValues(horizontal = 12.dp , vertical = 2.dp )
            ){
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically ,
                    modifier = Modifier
                ) {
                    Text("Critical" , fontSize = 20.sp , fontFamily = Bodyfont , color = Color.White)
                    Spacer(modifier = Modifier.size( (ScreenHeight * 0.009).dp ) )
                    Box(modifier = Modifier.background(color = Color.Red , shape = CircleShape ).size(12.dp)){}
                }

            }
            Spacer(modifier = Modifier.size( (ScreenHeight * 0.012).dp ) )
            Icon( imageVector = Icons.Default.Edit , contentDescription = "Edit Paitent list" , Modifier.size((ScreenHeight * 0.028).dp ))
        }


    }
}

@Composable
fun PaitentCard(patient : CardPatient ){
    @Composable
    fun ScreenHeight(k : Double) : Double = (LocalConfiguration.current.screenHeightDp * k)
    Card(Modifier.padding( bottom = ScreenHeight(0.02).dp ) .border(width = 0.5.dp,  color = Color.White.copy(alpha = 0.05f) , shape = RoundedCornerShape(12.dp))
    ){
        ConstraintLayout(modifier = Modifier.height( ScreenHeight(0.16).dp ).width( ScreenHeight(0.9).dp )
            .background(SecClr)
        ){
            val ( p_image , p_details , p_info) = createRefs()

            Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = ScreenHeight(0.01).dp)
                    .fillMaxHeight().fillMaxWidth(0.15f)
                    .constrainAs(p_image){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            ){

            }

            Column( verticalArrangement = Arrangement.SpaceEvenly ,
                modifier = Modifier.padding( top = (ScreenHeight(0.02).dp ))
                    .fillMaxHeight(0.9f).fillMaxWidth(0.65f)
                    .constrainAs(p_details){
                        start.linkTo(p_image.end)
                        top.linkTo(parent.top)

                    }
            ){
                Text( "Name : ${patient.name}" , style = TextStyle( fontSize = ScreenHeight(0.022).sp , fontFamily = Bodyfont) , color = Color.White  )
                Text( "Doctor name : ${patient.conditon}" , style = TextStyle( fontSize = ScreenHeight(0.022).sp , fontFamily = Bodyfont) , modifier = Modifier.fillMaxWidth() ,  color = Color.White)

                Row( horizontalArrangement = Arrangement.spacedBy( ScreenHeight(0.022).dp ) ,
                    modifier = Modifier.fillMaxWidth()) {
                    Text( "Age : ${patient.age}"  , style = TextStyle( fontSize = ScreenHeight(0.022).sp , fontFamily = Bodyfont) , color = Color.White)
                    Text( "Gender: ${patient.gender}", style = TextStyle( fontSize = ScreenHeight(0.022).sp , fontFamily = Bodyfont) , color = Color.White)
                }

            }

            Column( verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight(0.65f)
                    .constrainAs(p_info){
                        start.linkTo(p_details.end)
                        end.linkTo(parent.end , margin = 3.dp)
                        top.linkTo(parent.top , margin = 13.dp )
                        bottom.linkTo(parent.bottom , margin = 13.dp)
                    }
            ){
            }
        }
    }
}



@Composable
fun BottomNavBar(navController: NavController = rememberNavController() , barState: TopAppBarState) {
    val ScreenHeight = LocalConfiguration.current.screenHeightDp
    // BOTTOM BAR
      when(barState) {
          TopAppBarState.NurseDashBoard -> {
              ConstraintLayout(modifier = Modifier.fillMaxWidth()
              ){
                  val (btmbar, dcricle, addbtn) = createRefs()

                  val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                  Box(modifier = Modifier.background(color = bottombar, shape = btmbarshpae).border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                      .constrainAs(btmbar) {
                          start.linkTo(parent.start)
                          end.linkTo(parent.end)
                          bottom.linkTo(parent.bottom)
                      }.fillMaxWidth().height((ScreenHeight * 0.065).dp)
                  ) {

                      Row (modifier = Modifier.matchParentSize()
                          .background(color = SecClr, shape = btmbarshpae)
                          .padding(top = 12.dp , start = 50.dp , end = 50.dp),
                          horizontalArrangement = Arrangement.SpaceBetween
                      ){ // Try Converting a normal image into a vector
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.background( color = AppBg.copy( alpha = 0.52f) , shape = RoundedCornerShape(14.dp))
                          ) {
                              Icon(painter = painterResource(R.drawable.home),
                                  tint = HTextClr,
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Home" , style = TextStyle( fontFamily = Headingfont , color =  HTextClr),
                                  modifier = Modifier.padding(start =  6.dp , end = 6.dp , bottom = 6.dp)
                              )
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding(end = (ScreenHeight * 0.04).dp ).clickable{
                                  navController.navigate(route = Destinations.ShiftReportScreen.ref)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.key),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding( start = (ScreenHeight * 0.04).dp ).clickable{
                                  navController.navigate(route = Destinations.NurseNotes.ref)
                              }
                          ){

                              Icon(painter = painterResource(R.drawable.stickynote),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.036).dp)
                              )
                              Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.clickable{
                                  navController.navigate(Destinations.AccountScreen.ref)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                  tint = Color.White.copy(alpha = 0.55f),
                                  modifier = Modifier
                                      .size((ScreenHeight * 0.035).dp)
                              )
                              Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }






                      }

                  }




                  val bottomgline = createGuidelineFromBottom(0.30f)
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier.size((ScreenHeight * 0.08).dp)
                          .constrainAs(dcricle) {
                              bottom.linkTo(bottomgline)
                              start.linkTo(btmbar.start)
                              end.linkTo(btmbar.end)

                          }.clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          },
                      containerColor = AppBg,
                  ) {}
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier
                          .size((ScreenHeight * 0.06).dp)
                          .constrainAs(addbtn) {
                              start.linkTo(dcricle.start)
                              end.linkTo(dcricle.end)
                              bottom.linkTo(dcricle.bottom, margin = (ScreenHeight * 0.010).dp)
                          }.clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          },
                      containerColor = HTextClr,
                      contentColor = Color.White,
                  ) {
                      Icon(imageVector = Icons.Default.Add, contentDescription = "",
                          modifier = Modifier.size((ScreenHeight * 0.03).dp).clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          }
                      )
                  }


              }
          }
          TopAppBarState.ShitfReport -> {
              ConstraintLayout(modifier = Modifier.fillMaxWidth()
              ){
                  val (btmbar, dcricle, addbtn) = createRefs()

                  val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                  Box(modifier = Modifier.background(color = bottombar, shape = btmbarshpae).border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                      .constrainAs(btmbar) {
                          start.linkTo(parent.start)
                          end.linkTo(parent.end)
                          bottom.linkTo(parent.bottom)
                      }.fillMaxWidth().height((ScreenHeight * 0.065).dp)
                  ) {

                      Row (modifier = Modifier.matchParentSize()
                          .background(color = SecClr, shape = btmbarshpae)
                          .padding(top = 12.dp , start = 50.dp , end = 50.dp),
                          horizontalArrangement = Arrangement.SpaceBetween
                      ){ // Try Converting a normal image into a vector
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.clickable{
                                  navController.popBackStack(route = Destinations.NurseDboardScreen.ref , inclusive = false)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.home),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }

                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding(end = (ScreenHeight * 0.04).dp )
                                  .background(color = AppBg.copy(alpha = 0.52f) , shape = RoundedCornerShape(15.dp))
                          ) {
                              Icon(painter = painterResource(R.drawable.key),
                                  tint = HTextClr,
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = HTextClr) ,
                                  modifier = Modifier.padding(start =  6.dp , end = 6.dp , bottom = 6.dp)
                              )
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding( start = (ScreenHeight * 0.04).dp )
                                  .clickable{
                                  navController.popBackStack( route = Destinations.NurseDboardScreen.ref , false)
                                  navController.navigate(route = Destinations.NurseNotes.ref)
                              }
                          ){
                              Icon(painter = painterResource(R.drawable.stickynote),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.036).dp)
                              )
                              Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.clickable{
                                  navController.navigate(Destinations.AccountScreen.ref)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                  tint = Color.White.copy(alpha = 0.55f),
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp).clickable{
                                      navController.popBackStack( route = Destinations.NurseDboardScreen.ref , false)
                                      navController.navigate(route = Destinations.AccountScreen.ref)
                                  }
                              )
                              Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }






                      }

                  }




                  val bottomgline = createGuidelineFromBottom(0.30f)
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier.size((ScreenHeight * 0.08).dp)
                          .constrainAs(dcricle) {
                              bottom.linkTo(bottomgline)
                              start.linkTo(btmbar.start)
                              end.linkTo(btmbar.end)

                          }.clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          },
                      containerColor = AppBg,
                  ) {}
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier
                          .size((ScreenHeight * 0.06).dp)
                          .constrainAs(addbtn) {
                              start.linkTo(dcricle.start)
                              end.linkTo(dcricle.end)
                              bottom.linkTo(dcricle.bottom, margin = (ScreenHeight * 0.010).dp)
                          }.clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          },
                      containerColor = HTextClr,
                      contentColor = Color.White,
                  ) {
                      Icon(imageVector = Icons.Default.Add, contentDescription = "",
                          modifier = Modifier.size((ScreenHeight * 0.03).dp).clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          }
                      )
                  }


              }
          }

          TopAppBarState.NurseNotes -> {
              ConstraintLayout(modifier = Modifier.fillMaxWidth()
              ){
                  val (btmbar, dcricle, addbtn) = createRefs()

                  val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                  Box(modifier = Modifier.background(color = bottombar, shape = btmbarshpae).border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                      .constrainAs(btmbar) {
                          start.linkTo(parent.start)
                          end.linkTo(parent.end)
                          bottom.linkTo(parent.bottom)
                      }.fillMaxWidth().height((ScreenHeight * 0.065).dp)
                  ) {

                      Row (modifier = Modifier.matchParentSize()
                          .background(color = SecClr, shape = btmbarshpae)
                          .padding(top = 12.dp , start = 50.dp , end = 50.dp),
                          horizontalArrangement = Arrangement.SpaceBetween
                      ){ // Try Converting a normal image into a vector
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.clickable{
                                  navController.popBackStack(route = Destinations.NurseDboardScreen.ref , inclusive = false)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.home),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }


                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding(end = (ScreenHeight * 0.04).dp ).clickable{
                                  navController.popBackStack( route = Destinations.NurseDboardScreen.ref , false)
                                  navController.navigate(route = Destinations.ShiftReportScreen.ref)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.key),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding( start = (ScreenHeight * 0.04).dp )
                                  .background(AppBg.copy(alpha = 0.52f) , shape = RoundedCornerShape(14.dp))
                          ){

                              Icon(painter = painterResource(R.drawable.stickynote),
                                  tint = HTextClr,
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.036).dp)
                              )
                              Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.White),
                                  modifier = Modifier.padding(start =  6.dp , end = 6.dp , bottom = 6.dp)
                              )
                          }

                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.clickable{
                                  navController.popBackStack( route = Destinations.NurseDboardScreen.ref , false)
                                  navController.navigate(Destinations.AccountScreen.ref)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                  tint = Color.White.copy(alpha = 0.55f),
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }








                      }

                  }




                  val bottomgline = createGuidelineFromBottom(0.30f)
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier.size((ScreenHeight * 0.08).dp)
                          .constrainAs(dcricle) {
                              bottom.linkTo(bottomgline)
                              start.linkTo(btmbar.start)
                              end.linkTo(btmbar.end)

                          }.clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          },
                      containerColor = AppBg,
                  ) {}
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier
                          .size((ScreenHeight * 0.06).dp)
                          .constrainAs(addbtn) {
                              start.linkTo(dcricle.start)
                              end.linkTo(dcricle.end)
                              bottom.linkTo(dcricle.bottom, margin = (ScreenHeight * 0.010).dp)
                          }.clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          },
                      containerColor = HTextClr,
                      contentColor = Color.White,
                  ) {
                      Icon(imageVector = Icons.Default.Add, contentDescription = "",
                          modifier = Modifier.size((ScreenHeight * 0.03).dp).clickable {
                              navController.navigate(Destinations.PatientRegisterScreen.ref)
                          }
                      )
                  }


              }
          }


          TopAppBarState.Profile -> {
              ConstraintLayout(modifier = Modifier.fillMaxWidth()
              ){
                  val (btmbar, dcricle, addbtn) = createRefs()

                  val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                  Box(modifier = Modifier.background(color = bottombar, shape = btmbarshpae).border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                      .constrainAs(btmbar) {
                          start.linkTo(parent.start)
                          end.linkTo(parent.end)
                          bottom.linkTo(parent.bottom)
                      }.fillMaxWidth().height((ScreenHeight * 0.065).dp)
                  ) {

                      Row (modifier = Modifier.matchParentSize()
                          .background(color = SecClr, shape = btmbarshpae)
                          .padding(top = 12.dp , start = 50.dp , end = 50.dp),
                          horizontalArrangement = Arrangement.SpaceBetween
                      ){ // Try Converting a normal image into a vector
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.clickable{
                                  navController.popBackStack(route = Destinations.NurseDboardScreen.ref , inclusive = false)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.home),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }


                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding(end = (ScreenHeight * 0.04).dp ).clickable{
                                  navController.navigate(route = Destinations.ShiftReportScreen.ref)
                              }
                          ) {
                              Icon(painter = painterResource(R.drawable.key),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.035).dp)
                              )
                              Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.padding( start = (ScreenHeight * 0.04).dp ).clickable{
                                  navController.navigate(route = Destinations.NurseNotes.ref)
                              }
                          ){

                              Icon(painter = painterResource(R.drawable.stickynote),
                                  tint = Color.White.copy(alpha = 0.55f),
                                  contentDescription = "Paitent Image",
                                  modifier = Modifier.size((ScreenHeight * 0.036).dp)
                              )
                              Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.White))
                          }
                          Column(horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center,
                              modifier = Modifier.background(AppBg.copy(alpha = 0.52f) , shape = RoundedCornerShape(14.dp))
                          ) {
                              Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                  tint = HTextClr,
                                  modifier = Modifier
                                      .size((ScreenHeight * 0.035).dp)
                              )
                              Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White),
                                  modifier = Modifier.padding(start =  6.dp , end = 6.dp , bottom = 6.dp)
                              )
                          }






                      }

                  }




                  val bottomgline = createGuidelineFromBottom(0.30f)
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier.size((ScreenHeight * 0.08).dp)
                          .constrainAs(dcricle) {
                              bottom.linkTo(bottomgline)
                              start.linkTo(btmbar.start)
                              end.linkTo(btmbar.end)

                          }.clickable {
                              navController.navigate(Destinations.UpdateProfileScreen.ref)
                          },
                      containerColor = AppBg,
                  ) {}
                  FloatingActionButton(
                      onClick = {}, shape = CircleShape.copy(),
                      modifier = Modifier
                          .size((ScreenHeight * 0.06).dp)
                          .constrainAs(addbtn) {
                              start.linkTo(dcricle.start)
                              end.linkTo(dcricle.end)
                              bottom.linkTo(dcricle.bottom, margin = (ScreenHeight * 0.010).dp)
                          }.clickable {
                              navController.navigate(Destinations.UpdateProfileScreen.ref)
                          },
                      containerColor = HTextClr,
                      contentColor = Color.White,
                  ) {
                      Icon(imageVector = Icons.Default.Edit, contentDescription = "",
                          modifier = Modifier.size((ScreenHeight * 0.03).dp).clickable {
                              navController.navigate(Destinations.UpdateProfileScreen.ref)
                          }
                      )
                  }


              }



          }

          else -> Unit
      }

}


