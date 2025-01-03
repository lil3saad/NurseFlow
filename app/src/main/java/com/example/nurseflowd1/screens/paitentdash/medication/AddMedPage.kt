package com.example.nurseflowd1.screens.paitentdash.medication

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import com.example.nurseflowd1.ui.theme.AppBg
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.SecClr
import com.example.nurseflowd1.R
import com.example.nurseflowd1.datamodels.MedieneInfo
import com.example.nurseflowd1.screens.paitentdash.AlarmScheduler
import com.example.nurseflowd1.ui.theme.Bodyfont
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedScreen(modifier: Modifier = Modifier , navController: NavController,viewmodel : AppVM , patientid : String , patientname : String){




    Column(modifier = modifier.fillMaxSize().background(AppBg)
        , horizontalAlignment = Alignment.CenterHorizontally ) {

        Column(modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.9f)
            ,  horizontalAlignment = Alignment.CenterHorizontally ){

            val user_mediname = remember { mutableStateOf("") }
            var user_medinamestate : MutableState<SupportTextState> = remember { mutableStateOf(SupportTextState.ideal) }
            SingupFeilds(label = "", user_mediname, placeholdertext = "Enter Medicene Name", user_medinamestate)

            var user_MedType : MutableState<MediTypeState> = remember { mutableStateOf(MediTypeState.idle) }
            // MedType Select Row
            LazyRow(modifier = Modifier.fillMaxWidth()){
                item{
                    Button(
                        onClick = {
                            if(user_MedType.value != MediTypeState.tablet) user_MedType.value = MediTypeState.tablet
                            else user_MedType.value = MediTypeState.idle
                        },
                        modifier = Modifier.padding(8.dp)
                            .background( color = when(val state = user_MedType.value){
                                MediTypeState.tablet -> { HTextClr}
                                else -> { SecClr }
                            } , shape = RoundedCornerShape(5.dp))
                            .border(
                                width = 0.1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ){ Text("Tablet" , color = Color.White)  }
                }
                item{
                    Button(
                        onClick = {
                            if(user_MedType.value != MediTypeState.Tonic) user_MedType.value = MediTypeState.Tonic
                            else user_MedType.value = MediTypeState.idle

                        },
                        modifier = Modifier.padding(8.dp)
                            .background( color = when(val state = user_MedType.value){
                                MediTypeState.Tonic -> { HTextClr}
                                else -> { SecClr }
                            },shape = RoundedCornerShape(5.dp))
                            .border(
                                width = 0.1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ){ Text("Tonic" , color = Color.White)  }

                }
                item {
                    Button(
                        onClick = {
                            if(user_MedType.value != MediTypeState.Capsule) user_MedType.value = MediTypeState.Capsule
                            else user_MedType.value = MediTypeState.idle
                        },
                        modifier = Modifier.padding(8.dp)
                            .background( color = when(val state = user_MedType.value){
                                MediTypeState.Capsule -> { HTextClr}
                                else -> { SecClr }
                            },shape = RoundedCornerShape(5.dp))
                            .border(
                                width = 0.1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ){ Text("Capsule" , color = Color.White)  }

                }
                item{
                    Button(
                        onClick = {
                            if(user_MedType.value != MediTypeState.Drops) user_MedType.value = MediTypeState.Drops
                            else user_MedType.value = MediTypeState.idle

                        },
                        modifier = Modifier.padding(8.dp)
                            .background( color = when(val state = user_MedType.value){
                                MediTypeState.Drops -> { HTextClr}
                                else -> { SecClr }
                            },shape = RoundedCornerShape(5.dp))
                            .border(
                                width = 0.1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ){ Text("Drops" , color = Color.White)  }

                }
                item {

                    Button(
                        onClick = {
                            if (user_MedType.value != MediTypeState.Injection) user_MedType.value =
                                MediTypeState.Injection
                            else user_MedType.value = MediTypeState.idle

                        },
                        modifier = Modifier.padding(8.dp)
                            .background(
                                color = when (val state = user_MedType.value) {
                                    MediTypeState.Injection -> {
                                        HTextClr
                                    }

                                    else -> {
                                        SecClr
                                    }
                                }, shape = RoundedCornerShape(5.dp)
                            )
                            .border(
                                width = 0.1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) { Text("Injection", color = Color.White) }

                }

                item{
                    Button(
                        onClick = {
                            if(user_MedType.value != MediTypeState.Others) user_MedType.value = MediTypeState.Others
                            else user_MedType.value = MediTypeState.idle
              
                        },
                        modifier = Modifier.padding(8.dp)
                            .background( color = when(val state = user_MedType.value){
                                MediTypeState.Others -> { HTextClr}
                                else -> { SecClr }
                            },shape = RoundedCornerShape(5.dp))
                            .border(
                                width = 0.1.dp,
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ){ Text("Others" , color = Color.White)  }
                }
            }
            // Error Dialog
            if(user_MedType.value == MediTypeState.Empty){
                Row(modifier =  Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center){
                    Text(text = "Please Select a Medicine type" , fontSize = 12.sp , color = Color.Red)
                }

            }


            val currentdosage = remember { mutableStateOf(1) }
            val DosageMap = remember { mutableStateMapOf( currentdosage.value to "Set Dose") }
            val itemnumberforpicker = remember { mutableStateOf(1) }

            val launchPicker  = remember { mutableStateOf(false) }

            var ErrorTime = remember { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 18.dp).fillMaxWidth())  {
                DosageRow(currentdosage , DosageMap)

                // Timmer Box
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    LazyVerticalGrid(columns = GridCells.Fixed(3) ,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                    ){
                        for ( item in DosageMap.toMap()){
                            item {
                                SelectTimeButton(modifier = Modifier .clickable { if(!launchPicker.value) launchPicker.value = true
                                    else launchPicker.value = false
                                    itemnumberforpicker.value = item.key },
                                    item.key,
                                    item.value
                                )
                            }
                        }
                    }
                    if(launchPicker.value) {
                        LaunchTimeInput(
                            onConfrim = { timestate , itemnumber ->
                                DosageMap.replace(itemnumber , "${timestate.hour}:${timestate.minute}")
                                Log.d("MYCOCK","${DosageMap.toMap()} !278")
                                Log.d("TAGY" , "HOUR SET ${timestate.hour}")
                                Log.d("TAGY" , "HOUR SET ${timestate.minute}")
                                launchPicker.value = false
                            },
                            onCancel = { launchPicker.value = false },
                            itemnumber = itemnumberforpicker.value
                        )
                    }

                }
                if(ErrorTime.value){
                Row(modifier =  Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center)
                {
                    Text(text = "Please Set All Dosages Timings" , fontSize = 12.sp , color = Color.Red)
                }
                }
            }

            val isSwitchOn = remember { mutableStateOf(false) }
            val launchDatePicker = remember { mutableStateOf(false) }
            val getendtime : MutableState<Long> = remember {  mutableStateOf(6969)  }
            val isreminderset = remember { mutableStateOf("") }

            if(!launchPicker.value){
                Box {
                    Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Set Medicine Reminder Notifications?")
                        Switch(checked = isSwitchOn.value , onCheckedChange = {
                            isSwitchOn.value = it ; launchDatePicker.value = true } ,
                            colors = SwitchDefaults.colors(
                                uncheckedBorderColor = Color.White.copy(alpha = 0.35f),
                                uncheckedTrackColor = Color.Gray,
                                uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                                checkedTrackColor = HTextClr,
                                checkedThumbColor = Color.White,
                                checkedBorderColor = HTextClr)
                        )
                    }
                    if(isSwitchOn.value && !launchPicker.value) {
                        if(launchDatePicker.value){
                            val datestate = rememberDatePickerState(
                                initialDisplayMode = DisplayMode.Input,
                                initialSelectedDateMillis = System.currentTimeMillis(),
                                yearRange = 2024..2026
                            )
                            Column(modifier = Modifier.background(AppBg)){
                                DatePicker(datestate ,
                                    title = { Text("Duration") },
                                    showModeToggle = false,
                                    headline = { Text("Set Reminder duration until" , fontSize = 15.sp) },
                                    modifier = Modifier.background(AppBg),
                                    colors = DatePickerDefaults.colors(
                                        dividerColor = HTextClr,
                                        todayDateBorderColor = HTextClr,
                                        containerColor = AppBg,
                                        dateTextFieldColors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = AppBg,
                                            unfocusedIndicatorColor = Color.White,
                                            focusedIndicatorColor = HTextClr,
                                            unfocusedLabelColor = Color.White,
                                            focusedLabelColor = HTextClr,
                                            focusedContainerColor = AppBg,

                                        )
                                    )
                                )
                                Row( modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Button(onClick = { isSwitchOn.value = false
                                        launchDatePicker.value = false
                                    },
                                        colors = ButtonDefaults.buttonColors(containerColor = HTextClr
                                         , contentColor = Color.White
                                        ),
                                        modifier = Modifier.padding(start = 36.dp)
                                    ){ Text("Cancel") }
                                    Button(onClick = {
                                        getendtime.value = datestate.selectedDateMillis!!
                                        launchDatePicker.value = false
                                        isreminderset.value = EpochDateDisplay(datestate.selectedDateMillis!!)
                                    },
                                        colors = ButtonDefaults.buttonColors(containerColor = HTextClr
                                            , contentColor = Color.White),
                                        modifier = Modifier.padding(end = 36.dp)
                                    ) { Text("Confirm") }
                                }
                            }
                        }
                    }
                }
            }
            if(isreminderset.value.isNotBlank()){
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Remind Until : ${isreminderset.value}" , fontFamily = Bodyfont ,  color = HTextClr)
                }
            }
            //Display The Reminder Until


            val context = LocalContext.current
            Button(onClick = {
                if(IsValidateUserMed(ErrorTime,
                        user_mediname,
                        user_medinamestate,
                        user_MedType,
                        DosageMap)) {
                    for(item in DosageMap){
                        val mediinfo = MedieneInfo(medi_name = user_mediname.value,
                            medid = patientid+user_mediname.value,
                            medi_type = user_MedType.value.ref,
                            patientid = patientid,
                            dosageno = item.key,
                            meditime = item.value ,
                            patientname = patientname ,
                            endmeditime = getendtime.value)
                        val alarmscheduler = AlarmScheduler(context)
                        if(isSwitchOn.value) alarmscheduler.ScheduleAlarmNotification(mediinfo)
                        viewmodel.insertmedi(mediinfo)  // Insert very Dosage with time into room table
                    }
                    navController.popBackStack()
                }


            },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = HTextClr
                )

            ) {
                Text("Add Medicine")
            }
        }
    }
}

fun IsValidateUserMed(ErrorTime: MutableState<Boolean>,
                      medname : MutableState<String>,
                      mednamestate : MutableState<SupportTextState>,
                      medtype : MutableState<MediTypeState>,
                      DosageMap :  SnapshotStateMap<Int, String> ) : Boolean {

    var isvalid = true
    if(medname.value.isBlank()){
        mednamestate.value = SupportTextState.empty("Please enter medicine name")
        isvalid = false
    }else { mednamestate.value = SupportTextState.ideal }
    if(medtype.value == MediTypeState.idle || medtype.value == MediTypeState.Empty ){
        medtype.value = MediTypeState.Empty ; isvalid = false}
    for (item in DosageMap){
        if(item.value.contains("Set Dose")){
            ErrorTime.value = true
            isvalid = false
        }
    }
    return isvalid
}
@Composable
fun SelectTimeButton(
    modifier: Modifier,
    itemnumber : Int,
    itemvalue : String
){
    Row( modifier = modifier.padding(  top = 10.dp , start = 10.dp , end = 10.dp )
            .height(45.dp).width(10.dp)
            .background( SecClr , shape = RoundedCornerShape(12.dp))
            .border(0.2.dp , color = Color.White.copy(alpha = 0.1f) , shape = RoundedCornerShape(12.dp))
    ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Icon(imageVector = ImageVector.vectorResource(R.drawable.clock) , contentDescription = "Clock")
        Spacer( modifier = Modifier.size(5.dp))
        if(itemvalue == "Set Dose") Text("$itemvalue-$itemnumber" , fontSize = 12.sp)
        else Text("$itemvalue")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchTimeInput(
    onConfrim : (pickerstate : TimePickerState , itemnumber : Int )-> Unit ,
    onCancel : () -> Unit,
    itemnumber : Int
){
    val currentime : Calendar = Calendar.getInstance()

    val intimepickerstate = rememberTimePickerState(
        initialHour =  currentime.get(Calendar.HOUR_OF_DAY) ,
        initialMinute = currentime.get(Calendar.MINUTE),
        is24Hour =  false
    )
    Column(modifier = Modifier.fillMaxSize().background(AppBg) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        TimePicker(state = intimepickerstate,
            colors = TimePickerDefaults.colors(
                 timeSelectorUnselectedContentColor = Color.White,
                timeSelectorSelectedContentColor = Color.White,
                timeSelectorSelectedContainerColor = HTextClr,
                timeSelectorUnselectedContainerColor = SecClr ,
                periodSelectorBorderColor = Color.Transparent,
                periodSelectorSelectedContentColor = Color.White,
                periodSelectorSelectedContainerColor = HTextClr,
                periodSelectorUnselectedContainerColor = SecClr,
                clockDialColor = SecClr,
                clockDialSelectedContentColor = HTextClr,
                clockDialUnselectedContentColor = Color.White,
                selectorColor = Color.White,
            )
        )
        Row(  modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Button(onClick = {
                onCancel() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecClr,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(start = 36.dp)
            ) { Text("Cancel") }
            Button(onClick = {
                   onConfrim(intimepickerstate , itemnumber) }, colors = ButtonDefaults.buttonColors(
                containerColor = HTextClr,
                contentColor = Color.White
                 ),
                modifier = Modifier.padding(end = 36.dp)
            ) { Text("Confirm") }
        }
    }
}
@Composable
fun DosageRow(
    currentdosage : MutableState<Int>,
    itemmap : SnapshotStateMap<Int, String>
){
    Row( modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp) , verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.SpaceBetween){

        Text("Dosages (per/day)" , fontSize = 20.sp)


        Row( modifier = Modifier.background(SecClr , shape = RoundedCornerShape(55.dp))
            .border(0.55.dp , color = Color.White.copy(alpha = 0.1f) , shape = RoundedCornerShape(55.dp))
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){

            FloatingActionButton( onClick = {} ,
                modifier = Modifier.size(24.dp),
                containerColor = HTextClr) {
                Icon( imageVector = Icons.Filled.KeyboardArrowLeft , contentDescription = "Minus",
                modifier = Modifier.size(23.5.dp).clickable{
                    if(currentdosage.value != 1)
                    {
                        itemmap.remove(currentdosage.value)
                        currentdosage.value--
                    }
                    else Log.d("TAGY" , "Cant have Zero Dosages")
                },
                tint = Color.White)
            }
            Text(text = "${currentdosage.value}" , fontSize = 18.sp , modifier = Modifier.padding(horizontal = 8.dp))
            FloatingActionButton( onClick = {} ,
                modifier = Modifier.size(23.5.dp),
                containerColor = HTextClr){
                Icon( imageVector = Icons.Filled.Add , contentDescription = "Minus",
                    modifier = Modifier.size(25.dp)
                        .clickable{
                        if(currentdosage.value < 9) {
                            currentdosage.value++
                            itemmap.put(currentdosage.value , value = "Set Dose")

                        }
                        else  Log.d("TAGY" , "Cant have more then 9 Dosages")
                    },
                    tint = Color.White)
            }
        }

    }
}
fun EpochDateDisplay(
    epochMilliseconds: Long,
    pattern: String = "dd/MM/yyyy"
) : String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val date = java.util.Date(epochMilliseconds)
    val formattedDate = formatter.format(date)
    return formattedDate
}
