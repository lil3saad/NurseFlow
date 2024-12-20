package com.example.nurseflowd1.screens.nurseauth

import android.app.NotificationManager
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nurseflowd1.R
import com.example.nurseflowd1.datamodels.CardPatient
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.AuthState
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont
import com.example.nurseflowd1.ui.theme.SecClr
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.nurseflowd1.room.RoomPatientListState
import com.example.nurseflowd1.ui.theme.panelcolor
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState

// NURSE DASHBOARD
// if a Authenticated user coming from the login screen (AFTER NORMAL LOGIN ) is Authenticated and sent to N_Dashboard and the user keeps pressing back the user will be sent back to the login page , not for the first time , not for second time but he will be surely sent back
@Composable
fun NurseDashBoardScreen(modifier: Modifier , navController: NavController , viewmodel : AppVM , notificationmanager: NotificationManager) {

   val context = LocalContext.current

    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)

    // Change Bar States
    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("Dashboard"),
        colorState = AppBarColorState.NurseDashColors,
        iconState = NavigationIconState.None
    )
    viewmodel.ChangeBottomBarState(BottomBarState.NurseDashBoard)
    //Authentication State

    viewmodel.authStatus() // For AutoLog
    val authState by viewmodel.authstate.collectAsState() // Observing the AuthState
    val gotnursedocid by viewmodel.NurseDocId.collectAsState() // Get Nurse DocumentId by Uid


    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Idle -> { navController.navigate(Destinations.LoginScreen.ref) }
            is AuthState.UnAuthenticated -> { navController.navigate(Destinations.LoginScreen.ref) }
            is AuthState.LoginFailed -> { Toast.makeText(context, (authState as AuthState.LoginFailed).message, Toast.LENGTH_LONG).show() }
            is AuthState.Authenticated ->  {
                Log.d("TAGY" , "HOW IS THIS NURSE AUTHENTICATED")
                if (gotnursedocid is NurseDocIdState.CurrentNurseId) Unit
                else viewmodel.GetNurseDocId()
            }
            else -> Unit
        }
    }
    val roompatientliststate by viewmodel.cardpatientlist.collectAsState()
    LaunchedEffect(gotnursedocid) {
        if(gotnursedocid is NurseDocIdState.CurrentNurseId) {
            viewmodel.getCardPatietnList()
        }
    }
     // Just Get NurseId Once & Fetch paitents everytime NDASH Is Launched but only do this once
    Column(modifier = modifier.fillMaxSize().background(AppBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        val displaycriticallist : MutableState<Boolean> = remember { mutableStateOf(false) }
        val serachtext  : MutableState<String> = remember { mutableStateOf("") }

        val listsize : MutableState<String> = remember { mutableStateOf("0") }
        TopPanel(displaycriticallist , serachtext, viewmodel , listsize)

        LazyColumn(modifier = Modifier.padding(top = ScreenWidth(0.05).dp )
            .fillMaxWidth(0.9f).fillMaxHeight()) {
            when(val state = roompatientliststate){
                RoomPatientListState.idle -> { viewmodel.getCardPatietnList() }
                RoomPatientListState.NewAdded -> { viewmodel.getCardPatietnList() }
                is RoomPatientListState.FullReadList -> {
                         val patientlist = state.patientlist
                          listsize.value = patientlist.size.toString()
                         items(patientlist) {
                                 patient -> PaitentCard(patient , navController, notificationmanager)
                         }
                }
                RoomPatientListState.emptylist -> {
                    item {
                        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight() ,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No patients available. Please add patients.")
                        } }
                }
                RoomPatientListState.loading -> {
                    item{
                        Box(modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
                        }
                    }
                }
                is RoomPatientListState.CriticalList -> {
                    val patientlist = state.patientlist
                    listsize.value = patientlist.size.toString()
                    items(patientlist){
                            patient -> PaitentCard(patient , navController , notificationmanager)
                    }
                }
                is RoomPatientListState.SearchList -> {
                    val patientlist = state.patientlist
                    items(patientlist){
                            patient -> PaitentCard(patient , navController , notificationmanager)
                    }
                }
                is RoomPatientListState.Error -> {
                    item { Text("${state.msg}") }
                }
            }
        }
    }
}

@Composable
fun TopPanel(criticaliststate :  MutableState<Boolean> , Searchtext : MutableState<String> , viewmodel: AppVM,
             listsize : MutableState<String>
) {

    val softwarekeybaord = LocalSoftwareKeyboardController.current!!
    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)
    val toppanelshape = RoundedCornerShape(bottomEnd = 45.dp , bottomStart = 45.dp)


    var displaytext =  "Total patients"
    var displaylabel = "Critical"
    if(criticaliststate.value){
        displaytext = "Critical Patients"
        displaylabel = "Total"
    }
    Column(modifier = Modifier.fillMaxWidth().fillMaxWidth(0.5f)
        .background(panelcolor, shape = toppanelshape)
        .padding(vertical = 10.dp, horizontal = 20.dp)
    ){

        Card( colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )){
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp)
            ){
                Text(displaytext , fontSize = 25.sp , fontFamily = Bodyfont , color = Color.White)
                Row(modifier = Modifier.fillMaxWidth() ,
                    horizontalArrangement = Arrangement.SpaceBetween ,
                    verticalAlignment = Alignment.Bottom){

                    Text(listsize.value, fontSize = 55.sp , fontFamily = Headingfont , color = Color.White ,
                        modifier = Modifier.padding(start = 10.dp )
                        )
                    Button( onClick = { if(criticaliststate.value == false ){
                                               criticaliststate.value = true
                                               viewmodel.getCriticalList()
                                           }else {
                                               viewmodel.getCardPatietnList()
                                               criticaliststate.value = false
                                           }
                                      },
                        colors = if(criticaliststate.value) {
                            ButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.15f),
                                contentColor = Color.Black ,
                                disabledContentColor = Color.White.copy(alpha = 0.22f),
                                disabledContainerColor = Color.White.copy(alpha = 0.22f)
                            )
                        }else {
                            ButtonColors(
                                containerColor = Color.Red.copy(alpha = 0.12f),
                                contentColor = Color.Black ,
                                disabledContentColor = Color.White.copy(alpha = 0.22f),
                                disabledContainerColor = Color.White.copy(alpha = 0.22f)
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 12.dp , vertical = 2.dp )
                    ){
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically ,
                            modifier = Modifier
                        ) {
                            Text( displaylabel , fontSize = 15.sp  , fontFamily = Bodyfont , color = Color.White)
                            Spacer(modifier = Modifier.size( 8.dp ) )
                            if( criticaliststate.value){
                                Box(modifier = Modifier.background(color = Color.White , shape = CircleShape ).size( 9.dp ) ){}
                            }else Box(modifier = Modifier.background(color = Color.Red , shape = CircleShape ).size( 9.dp ) ){}
                        }
                    }
                }
            }
        }
        // Search Row
        var isSearch by remember { mutableStateOf(true) }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField( value = Searchtext.value ,
                onValueChange = { usertext -> Searchtext.value = usertext },
                modifier = Modifier
                    .fillMaxWidth()
                    .height( 55.dp ),
                placeholder = { Text("Search patient" , color = Color.White.copy(alpha = 0.5f) )
                              } ,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.22f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(alpha = 0.12f),
                    focusedIndicatorColor = Color.Transparent,
                ),
                trailingIcon = {
                    if(isSearch){
                        Icon( imageVector = Icons.Default.Search , contentDescription = "Search Patiens" , modifier = Modifier
                            .padding(end = 12.dp)
                            .size( 38.dp ).clickable{
                                isSearch = false
                                val username = "%${Searchtext.value}%"
                                viewmodel.getSearchResult(username)
                                Searchtext.value = ""
                                softwarekeybaord.hide()
                            } ,
                            tint =Color.White)
                    }else {
                        Icon( imageVector = Icons.Default.Close , contentDescription = "Search Patiens" , modifier = Modifier
                            .padding(end = 12.dp)
                            .size( 38.dp )
                            .clickable{
                                viewmodel.getCardPatietnList()
                                isSearch = true
                            }
                            ,
                            tint = HTextClr)
                    }
                },
                shape =  RoundedCornerShape(45.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Unspecified,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                         isSearch = false
                         val username = "%${Searchtext.value}%"
                         viewmodel.getSearchResult(username)
                         Searchtext.value = ""
                         softwarekeybaord.hide()
                    }
                )
            )

        }
    }
}
@Composable
fun PaitentCard(patient : CardPatient , navigator: NavController , notificationmanager: NotificationManager){
    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)
    val context = LocalContext.current

    Card(Modifier.padding( bottom = 10.dp )
        .height(160.dp)
        .border(width = 0.5.dp,  color = Color.White.copy(alpha = 0.05f) , shape = RoundedCornerShape(12.dp))
    ){
        Row(modifier = Modifier.fillMaxSize() .background(color = SecClr)){
            Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
                    .fillMaxWidth(0.18f)
                    .padding(start = ScreenWidth(0.02).dp , end = ScreenWidth(0.03).dp )

            ){
                Image( imageVector = ImageVector.vectorResource(R.drawable.patientpic) , contentDescription = "PaitentPicture"
                    ,modifier = Modifier.size( 65.dp ).clickable{}
                )
            }
            Column( verticalArrangement = Arrangement.Center ,
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f)

            ){
                Text( "Name : ${patient.name}" , style = TextStyle( fontSize = 18.sp , fontFamily = Bodyfont) , color = Color.White  )
                Text( "Doctor : ${patient.doctorname}" , style = TextStyle( fontSize = 18.sp , fontFamily = Bodyfont) , modifier = Modifier.fillMaxWidth() ,  color = Color.White)
                Text( "Conditon: ${patient.condition}" , style = TextStyle( fontSize = 18.sp , fontFamily = Bodyfont) , modifier = Modifier.fillMaxWidth() ,  color = Color.White)

                Row( horizontalArrangement = Arrangement.spacedBy( ScreenWidth(0.05).dp ) ,
                    modifier = Modifier.fillMaxWidth()) {
                    Text( "Age : ${patient.age}"  , style = TextStyle( fontSize = 15.sp , fontFamily = Bodyfont) , color = Color.White)
                    Text( "Gender: ${patient.gender}", style = TextStyle( fontSize = 15.sp , fontFamily = Bodyfont) , color = Color.White)
                }
            }

            Column( verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
                    .padding( top = 12.dp , bottom = 9.dp , start = 8.dp , end = 8.dp )) {
                Icon( imageVector = ImageVector.vectorResource(R.drawable.dashopen),
                    contentDescription = "DashboardIcon",
                    tint = Color.White,
                    modifier = Modifier.size( 38.dp ).clickable {
                        navigator.navigate(route = "patientdash/${patient.patientid}/${patient.name}")
                    }
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Ward" ,color = Color.White , fontSize = 13.sp)
                    Text(patient.wardno, color = Color.White , fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun MyBottomNavBar(navController: NavController = rememberNavController(), bottombarstate : BottomBarState ){
    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)

    when(bottombarstate){
        BottomBarState.NurseDashBoard -> {
            ConstraintLayout(modifier = Modifier.fillMaxWidth().background(AppBg)
            ){
                val (btmbar, dcricle, addbtn) = createRefs()

                val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                Box(modifier = Modifier.background(color = SecClr, shape = btmbarshpae)
                    .border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                    .constrainAs(btmbar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.fillMaxWidth()
                    .height( ScreenWidth(0.15).dp )
                ) {

                    Row (modifier = Modifier.matchParentSize()
                        .background(color = SecClr, shape = btmbarshpae)
                        .padding(start = 50.dp , end = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(painter = painterResource(R.drawable.home),
                                tint = HTextClr,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color =  HTextClr , fontSize = 9.sp)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = ScreenWidth(0.1).dp )
                                .clickable{
                                    navController.navigate(route = Destinations.ShiftReportScreen.ref)
                                }
                        ) {
                            Icon(painter = painterResource(R.drawable.key),
                                tint = Color.White.copy(alpha = 0.55f),
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.White, fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                                .clickable{
                                    navController.navigate(route = Destinations.NurseNotes.ref)
                                }
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint = Color.White.copy(alpha = 0.55f),
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.White, fontSize = 9.sp))
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
                                    .size( ScreenWidth(0.08).dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White, fontSize = 9.sp))
                        }
                    }
                }
                val bottomgline = createGuidelineFromBottom(0.25f)
                FloatingActionButton(
                    onClick = {}, shape = CircleShape.copy(),
                    modifier = Modifier.size( 70.dp )
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
                    modifier = Modifier.border(0.2.dp , color = Color.White.copy(alpha = 0.5f) , shape = CircleShape)
                        .size( 56.dp )
                        .constrainAs(addbtn) {
                            start.linkTo(dcricle.start)
                            end.linkTo(dcricle.end)
                            bottom.linkTo(dcricle.bottom, margin = 7.dp )
                        }.clickable {
                            navController.navigate(Destinations.PatientRegisterScreen.ref)
                        },
                    containerColor = HTextClr,
                    contentColor = Color.White,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "",
                        modifier = Modifier.size( 25.dp )
                            .clickable {
                                navController.navigate(Destinations.PatientRegisterScreen.ref)
                            }
                    )
                }


            }
        }
        BottomBarState.NotesPage -> {
            ConstraintLayout(modifier = Modifier.fillMaxWidth().background(AppBg)
            ){
                val (btmbar, dcricle, addbtn) = createRefs()

                val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                Box(modifier = Modifier.background(color = SecClr, shape = btmbarshpae)
                    .border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                    .constrainAs(btmbar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.fillMaxWidth()
                    .height( ScreenWidth(0.15).dp )
                ) {
                    Row (modifier = Modifier.matchParentSize()
                        .background(color = SecClr, shape = btmbarshpae)
                        .padding(start = 50.dp , end = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.home),
                                tint = Color.White.copy(alpha = 0.55f),
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.White , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = ScreenWidth(0.1).dp )
                                .clickable{
                                    navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                    navController.navigate( route = Destinations.ShiftReportScreen.ref )
                                }
                        ){
                            Icon(painter = painterResource(R.drawable.key),
                                tint = Color.White.copy(alpha = 0.55f) ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.White ,  fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint = HTextClr ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont ,  color =  HTextClr, fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                navController.navigate(Destinations.AccountScreen.ref)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                tint = Color.White.copy(alpha = 0.55f),
                                modifier = Modifier
                                    .size( ScreenWidth(0.08).dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White, fontSize = 9.sp))
                        }
                    }

                }




                val bottomgline = createGuidelineFromBottom(0.25f)
                FloatingActionButton(
                    onClick = {}, shape = CircleShape.copy(),
                    modifier = Modifier.size( 70.dp )
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
                    modifier = Modifier.border(0.2.dp , color = Color.White.copy(alpha = 0.5f) , shape = CircleShape)
                        .size( 56.dp )
                        .constrainAs(addbtn) {
                            start.linkTo(dcricle.start)
                            end.linkTo(dcricle.end)
                            bottom.linkTo(dcricle.bottom, margin = 7.dp )
                        }.clickable {
                            navController.navigate(Destinations.PatientRegisterScreen.ref)
                        },
                    containerColor = HTextClr,
                    contentColor = Color.White,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "",
                        modifier = Modifier.size( 25.dp )
                            .clickable {
                                navController.navigate(Destinations.PatientRegisterScreen.ref)
                            }
                    )
                }


            }
        }
        BottomBarState.ReportsPage -> {
            ConstraintLayout(modifier = Modifier.fillMaxWidth().background(AppBg)
            ){
                val (btmbar, dcricle, addbtn) = createRefs()

                val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                Box(modifier = Modifier.background(color = SecClr, shape = btmbarshpae)
                    .border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                    .constrainAs(btmbar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.fillMaxWidth()
                    .height( ScreenWidth(0.15).dp )
                ) {

                    Row (modifier = Modifier.matchParentSize()
                        .background(color = SecClr, shape = btmbarshpae)
                        .padding(start = 50.dp , end = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable{
                                    navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                }
                        ) {
                            Icon(painter = painterResource(R.drawable.home),
                                tint = Color.White.copy(alpha = 0.55f) ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.White , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = ScreenWidth(0.1).dp )
                        ){
                            Icon(painter = painterResource(R.drawable.key),
                                tint = HTextClr ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color =  HTextClr ,  fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                                .clickable{
                                    navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                    navController.navigate(route = Destinations.NurseNotes.ref)
                                }
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint = Color.White.copy(alpha = 0.55f),
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.White, fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                navController.navigate(Destinations.AccountScreen.ref)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                tint = Color.White.copy(alpha = 0.55f),
                                modifier = Modifier
                                    .size( ScreenWidth(0.08).dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.White, fontSize = 9.sp))
                        }
                    }

                }


                val bottomgline = createGuidelineFromBottom(0.25f)
                FloatingActionButton(
                    onClick = {}, shape = CircleShape.copy(),
                    modifier = Modifier.size( 70.dp )
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
                    modifier = Modifier.border(0.2.dp , color = Color.White.copy(alpha = 0.5f) , shape = CircleShape)
                        .size( 56.dp )
                        .constrainAs(addbtn) {
                            start.linkTo(dcricle.start)
                            end.linkTo(dcricle.end)
                            bottom.linkTo(dcricle.bottom, margin = 7.dp )
                        }.clickable {
                            navController.navigate(Destinations.PatientRegisterScreen.ref)
                        },
                    containerColor = HTextClr,
                    contentColor = Color.White,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "",
                        modifier = Modifier.size( 25.dp )
                            .clickable {
                                navController.navigate(Destinations.PatientRegisterScreen.ref)
                            }
                    )
                }


            }
        }
        BottomBarState.AccountPage -> {
            ConstraintLayout(modifier = Modifier.fillMaxWidth().background(AppBg)
            ){
                val (btmbar, dcricle, addbtn) = createRefs()

                val btmbarshpae = RoundedCornerShape(topEnd = 45.dp , topStart = 45.dp)

                Box(modifier = Modifier.background(color = SecClr, shape = btmbarshpae)
                    .border(0.1.dp , color = Color.White.copy(alpha = 0.05f) , shape = btmbarshpae)
                    .constrainAs(btmbar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.fillMaxWidth()
                    .height( ScreenWidth(0.15).dp )
                ) {

                    Row (modifier = Modifier.matchParentSize()
                        .background(color = SecClr, shape = btmbarshpae)
                        .padding(start = 50.dp , end = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.home),
                                tint = Color.White.copy(alpha = 0.55f) ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.White , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = ScreenWidth(0.1).dp )
                                .clickable{
                                    navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                    navController.navigate( route = Destinations.ShiftReportScreen.ref )
                                }
                        ){
                            Icon(painter = painterResource(R.drawable.key),
                                tint = Color.White.copy(alpha = 0.55f) ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.White ,  fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint =  Color.White.copy(alpha = 0.55f) ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( ScreenWidth(0.08).dp )
                                    .clickable{
                                        navController.popBackStack( route = Destinations.NurseDboardScreen.ref , false)
                                        navController.navigate(Destinations.NurseNotes.ref)
                                    }
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont ,   color = Color.White , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                navController.navigate(Destinations.AccountScreen.ref)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                tint = HTextClr ,
                                modifier = Modifier
                                    .size( ScreenWidth(0.08).dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color =  HTextClr, fontSize = 9.sp))
                        }
                    }

                }

                val bottomgline = createGuidelineFromBottom(0.25f)
                FloatingActionButton(
                    onClick = {}, shape = CircleShape.copy(),
                    modifier = Modifier.size( 70.dp )
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
                    modifier = Modifier.border(0.2.dp , color = Color.White.copy(alpha = 0.5f) , shape = CircleShape)
                        .size( 56.dp )
                        .constrainAs(addbtn) {
                            start.linkTo(dcricle.start)
                            end.linkTo(dcricle.end)
                            bottom.linkTo(dcricle.bottom, margin = 7.dp )
                        }.clickable {
                            navController.navigate(Destinations.UpdateProfileScreen.ref)
                        },
                    containerColor = HTextClr,
                    contentColor = Color.White,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "",
                        modifier = Modifier.size( 25.dp )
                            .clickable {
                                navController.navigate(Destinations.UpdateProfileScreen.ref)
                            }
                    )
                }


            }
        }
        BottomBarState.NoBottomBar -> Unit
        else -> Unit
    }
}

