@file:Suppress("PreviewAnnotationInFunctionWithParameters")

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.screens.paitentdash.medication.EpochDateDisplay

// NURSE DASHBOARD
// if a Authenticated user coming from the login screen (AFTER NORMAL LOGIN ) is Authenticated and sent to N_Dashboard and the user keeps pressing back the user will be sent back to the login page , not for the first time , not for second time but he will be surely sent back
@Composable
fun NurseDashBoardScreen(modifier: Modifier , navController: NavController , viewmodel : AppVM ) {
    Log.d("TAGY" , "DASHBOARD SCREEN CALLED")
   val context = LocalContext.current
    viewmodel.authStatus() // For AutoLog
    val authState by viewmodel.authstate.collectAsState()
    val gotnursedocid by viewmodel.NurseDocId.collectAsState() // Get Nurse DocumentId by Uid// Observing the AuthState
    LaunchedEffect(authState) { // What Does Launched Effect Do Exactly
        when (authState) {
            is AuthState.Idle -> {
                Log.d("TAGY" , "NURSE IS NOT FUCKING AUTHED !NurseDash:120")
                navController.popBackStack() ; navController.navigate(Destinations.LoginScreen.ref)  }
            is AuthState.UnAuthenticated -> {  // Clears all BackStack
                Log.d("TAGY" , "NURSE IS NOT FUCKING AUTHED !NurseDash:120")
                navController.popBackStack()
                navController.navigate(Destinations.LoginScreen.ref)
            }
            is AuthState.LoginFailed -> { Toast.makeText(context, (authState as AuthState.LoginFailed).message, Toast.LENGTH_LONG).show() }
            is AuthState.Authenticated ->  {
                Log.d("TAGY" , "NURSE AUTHENTICATED !NurseDash:111")
                when (gotnursedocid) {
                    is  NurseDocIdState.CurrentNurseId -> Unit
                    else ->  viewmodel.GetNurseDocId()
                }
            }
            else -> {
                Log.d("TAGY" , "NURSE DONT KNOW WHICH State !NurseDash:120")
                navController.popBackStack()
                navController.navigate(Destinations.LoginScreen.ref)
            }
        }
    }
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

    LaunchedEffect(gotnursedocid) {
        when (gotnursedocid) {
            is NurseDocIdState.CurrentNurseId -> {
                Log.d("TAGY" , "FROM NurseDocState  NURSEDASH: 123")
                viewmodel.getCardPatietnList()
            }
            else -> Unit
        }
    }
    val roompatientliststate by viewmodel.cardpatientlist.collectAsState()
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
                is RoomPatientListState.idle -> { Log.d("TAGY" , "THIS IS WHY FROM ROOM LIST IDLE !NURSEDASH : 143" ) }
                is RoomPatientListState.NewAdded -> {
                    Log.d("TAGY" , "New Patient Called !NurseDash:165") ; viewmodel.getCardPatietnList() }
                is RoomPatientListState.FetchedList -> {
                         val patientlist = state.patientlist
                          listsize.value = patientlist.size.toString()
                         items(patientlist) {
                                 patient -> PaitentCard(patient , navController)
                         }
                }
                is RoomPatientListState.emptylist -> {
                    item {
                        listsize.value = "0"
                        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight() ,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No patients available. Please add patients.")
                        } }
                }
                is RoomPatientListState.loading -> {
                    item{
                        Box(modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator( modifier = Modifier.size(50.dp) , color = HTextClr , strokeWidth = 5.dp)
                        }
                    }
                }
                is RoomPatientListState.Error -> {
                    item { Text("${state.msg}") }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopPanel(criticaliststate :  MutableState<Boolean> , Searchtext : MutableState<String> , viewmodel: AppVM,
             listsize : MutableState<String>) {

    val softwarekeybaord = LocalSoftwareKeyboardController.current!!
    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)
    val toppanelshape = RoundedCornerShape(bottomEnd = 50.dp , bottomStart = 50.dp)


    var displaytext =  "Total patients"
    var displaylabel = "Critical"
    if(criticaliststate.value){
        displaytext = "Critical Patients"
        displaylabel = "Total"
    }
    Column(modifier = Modifier.fillMaxWidth().fillMaxWidth(0.5f)
        .background(HTextClr, shape = toppanelshape)
        .padding(vertical = 10.dp, horizontal = 20.dp)
    ){

        Card( colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )){
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp)
            ){
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(displaytext , fontSize = 25.sp , fontFamily = Bodyfont , color = Color.White )

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
                                containerColor = Color.Red.copy(alpha = 0.2f),
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

                Row(modifier = Modifier.fillMaxWidth() ,
                    horizontalArrangement = Arrangement.SpaceBetween ,
                    verticalAlignment = Alignment.Bottom){

                    Text(listsize.value, fontSize = 55.sp , fontFamily = Headingfont , color = Color.White ,
                        modifier = Modifier.padding(start = 10.dp ))

                    val context = LocalContext.current
                    var OptionsList = arrayOf("Name" , "Admission" )
                    var ExpandedBox by remember { mutableStateOf(false) }
                    var SelectedText by remember { mutableStateOf(OptionsList[1]) }

                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text("Sort" , modifier = Modifier.padding(end = 8.dp) , style = TextStyle(fontFamily = Headingfont) , color = Color.White)
                        Box(contentAlignment = Alignment.Center){
                            ExposedDropdownMenuBox(expanded = ExpandedBox , onExpandedChange = { ExpandedBox = !ExpandedBox }) {
                                Row(verticalAlignment = Alignment.CenterVertically , modifier = Modifier.background(shape = RoundedCornerShape(45.dp), color = Color.Black.copy(alpha = 0.10f)).padding(vertical = 0.dp , horizontal = 12.dp) ){
                                    Button(onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent , contentColor = Color.White) ,
                                        contentPadding = PaddingValues(0.dp)
                                        ){
                                        Text(SelectedText ,   style = TextStyle(fontSize = 10.sp , fontFamily = Bodyfont) , modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable , enabled = true) , color = Color.White)
                                    }
                                    Icon(imageVector = Icons.Default.ArrowDropDown , contentDescription = "Drop Down Arrow" , modifier = Modifier , tint = Color.White)
                                }
                                ExposedDropdownMenu(expanded = ExpandedBox , onDismissRequest = {ExpandedBox = false},
                                        containerColor = Color.Black.copy(alpha = 0.5f)  ,
                                        shape = RoundedCornerShape( topStart = 0.dp , topEnd = 0.dp , bottomStart = 15.dp , bottomEnd = 15.dp),
                                        matchTextFieldWidth = true,
                                        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(1.dp, color = Color.Black.copy(alpha = 0.1f))  ){
                                        OptionsList.forEachIndexed { index , value ->
                                            DropdownMenuItem(
                                                text = { Text("$value" , fontSize = 12.sp)} ,
                                                onClick = {
                                                    if(value == "Admission") viewmodel.getSortedListByDOA()
                                                    if(value == "Name") viewmodel.getSortedListByName()
                                                    SelectedText = value
                                                    ExpandedBox = false

                                                }
                                            )
                                        }
                                }
                            }
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
                placeholder = { Text("Search by name,department,condition,doctor" , color = Color.White  , fontSize = 12.sp)
                              } ,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                trailingIcon = {
                    if(isSearch){
                        Icon( imageVector = Icons.Default.Search , contentDescription = "Search Patients" , modifier = Modifier
                            .padding(end = 12.dp)
                            .size( 38.dp ).clickable{
                                isSearch = false
                                val username = "%${Searchtext.value}%"
                                viewmodel.getSearchResult(username)
                                Searchtext.value = ""
                                softwarekeybaord.hide()
                            } ,
                            tint = Color.White)
                    }else {
                        Icon( imageVector = Icons.Default.Close , contentDescription = "Search Patiens" , modifier = Modifier
                            .padding(end = 12.dp)
                            .size( 38.dp )
                            .clickable{
                                viewmodel.getCardPatietnList()
                                isSearch = true
                            },
                            tint = Color.White)
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
fun PaitentCard(patient : CardPatient , navigator: NavController){
    @Composable
    fun ScreenWidth(k : Double) : Double = (LocalConfiguration.current.screenWidthDp * k)
    val context = LocalContext.current

    Card(Modifier.padding( bottom = 10.dp)
        .height(180.dp)
        .border(width = 0.1.dp,  color = Color.Black.copy(alpha = 0.1f) , shape = RoundedCornerShape(12.dp))
        .clickable{
            navigator.navigate(route = "patientdash/${patient.patientid}/${patient.name}")
        },
        colors = CardDefaults.cardColors(containerColor = SecClr)
    ){
        Column(modifier = Modifier.fillMaxSize().padding(12.dp).background(color = SecClr) , verticalArrangement = Arrangement.SpaceBetween){
            Row(modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween){
                Image( imageVector = ImageVector.vectorResource(R.drawable.p_icon2) , contentDescription = "PaitentPicture",modifier = Modifier.size( 54.dp ).padding(end = 3.dp))
                Column(modifier = Modifier.fillMaxWidth(0.8f)){
                    Text( "Name : ${patient.name}" , style = TextStyle( fontSize = 16.sp , fontFamily = Bodyfont) ,  color =  Color.DarkGray )
                    Row( horizontalArrangement = Arrangement.spacedBy( ScreenWidth(0.05).dp ) ) {
                        Text( "Age : ${patient.age}"  , style = TextStyle( fontSize = 15.sp , fontFamily = Bodyfont) , color =  Color.Black.copy(alpha = 0.7f) )
                        Text( "Gender: ${patient.gender}", style = TextStyle( fontSize = 15.sp , fontFamily = Bodyfont) , color =  Color.Black.copy(alpha = 0.7f) )
                    }
                }
                Column(modifier = Modifier.size(45.dp).background(color = HTextClr , shape = RoundedCornerShape(12.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center){
                Text("Ward" ,color = Color.White , fontSize = 11.sp  , lineHeight = 11.sp)
                Text(patient.wardno, color = Color.White, fontSize = 11.sp ,  lineHeight = 11.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween ){
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp , top = 5.dp) ) {
                    Text( "Condition : ${patient.condition}" , style = TextStyle( fontSize = 16.sp , fontFamily = Bodyfont)  ,  color =  Color.Black.copy(alpha = 0.7f) )
                    Text( "Doctor : ${patient.doctorname}" , style = TextStyle( fontSize = 16.sp , fontFamily = Bodyfont) ,  color =  Color.Black.copy(alpha = 0.7f) )
                    Text( "Department : ${patient.department}" , style = TextStyle( fontSize = 16.sp , fontFamily = Bodyfont)  ,  color =  Color.Black.copy(alpha = 0.7f) )
                }
            }


            Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.End){
                var MyAddmisonDate =  EpochDateDisplay(patient.admissionDate)
                Text( "DOA: $MyAddmisonDate" , style = TextStyle( fontSize = 12.sp , fontFamily = Bodyfont) ,  color = Color.Black.copy(alpha = 0.7f) )
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
                                modifier = Modifier.size(34.dp )
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
                                tint = Color.Gray ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp)
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color =  Color.DarkGray , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                                .clickable{
                                    navController.navigate(route = Destinations.NurseNotes.ref)
                                }
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint =  Color.Gray ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 35.dp )
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.navigate(Destinations.AccountScreen.ref)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                tint =  Color.Gray ,
                                modifier = Modifier.size( 37.dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color =  Color.DarkGray , fontSize = 9.sp))
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
                        modifier = Modifier.size( 40.dp )
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
                                tint = Color.Gray ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = ScreenWidth(0.1).dp )
                        ){
                            Icon(painter = painterResource(R.drawable.key),
                                tint = HTextClr ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp )
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
                                tint = Color.Gray ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 35.dp )
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable{
                                navController.popBackStack(route = Destinations.NurseDboardScreen.ref, false)
                                navController.navigate(Destinations.AccountScreen.ref)
                            }
                        ) {
                            Icon(painter = painterResource(R.drawable.people), contentDescription = "",
                                tint = Color.Gray,
                                modifier = Modifier.size( 37.dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
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
                                tint = Color.Gray,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
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
                                tint = Color.Gray ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp )
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color =  Color.DarkGray ,  fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint = HTextClr ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 35.dp )
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
                                tint = Color.Gray,
                                modifier = Modifier.size( 37.dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
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
                                tint = Color.Gray  ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp )
                            )
                            Text("Home" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray , fontSize = 9.sp))
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
                                tint = Color.Gray  ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 34.dp )
                            )
                            Text("Reports" , style = TextStyle( fontFamily = Headingfont , color = Color.DarkGray ,  fontSize = 9.sp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding( start = ScreenWidth(0.1).dp )
                        ){

                            Icon(painter = painterResource(R.drawable.stickynote),
                                tint =  Color.Gray  ,
                                contentDescription = "Paitent Image",
                                modifier = Modifier.size( 35.dp )
                                    .clickable{
                                        navController.popBackStack( route = Destinations.NurseDboardScreen.ref , false)
                                        navController.navigate(Destinations.NurseNotes.ref)
                                    }
                            )
                            Text("Notes" , style = TextStyle( fontFamily = Headingfont ,   color = Color.DarkGray , fontSize = 9.sp))
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
                                    .size( 37.dp )
                            )
                            Text("Account" , style = TextStyle( fontFamily = Headingfont , color =  HTextClr, fontSize = 9.sp))
                        }
                    }

                }

                val bottomgline = createGuidelineFromBottom(0.25f)
                FloatingActionButton(onClick = {}, shape = CircleShape.copy(),
                    modifier = Modifier.size( 70.dp )
                        .constrainAs(dcricle) {
                            bottom.linkTo(bottomgline)
                            start.linkTo(btmbar.start)
                            end.linkTo(btmbar.end)
                        }.clickable { navController.navigate(Destinations.UpdateProfileScreen.ref) },
                    containerColor = AppBg,
                ){}
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
                    contentColor = Color.White){
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.editaccount), contentDescription = "",
                        modifier = Modifier.padding(start = 2.dp ).size( 28.dp ).clickable {
                                navController.navigate(Destinations.UpdateProfileScreen.ref)
                            })
                }


            }
        }
        BottomBarState.NoBottomBar -> Unit
        else -> Unit
    }
}

