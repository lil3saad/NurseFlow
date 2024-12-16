package com.example.nurseflowd1.screens.paitentdash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.datamodels.PatientInfo
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.R
import com.example.nurseflowd1.datamodels.MedieneInfo
import com.example.nurseflowd1.screens.Destinations
import com.example.nurseflowd1.screens.paitentdash.medication.MediTypeState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.nurseflowd1.room.RoomPatientListState
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont
import io.appwrite.models.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun PatientDashBoardScreen(modifier: Modifier = Modifier, viewmodel : AppVM, navcontroller: NavController,
                                   patientid: String, patientname : String
){

    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("$patientname"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.DefaultBack
    )
    viewmodel.ChangeBottomBarState(barstate = BottomBarState.NoBottomBar)

    viewmodel.patientmedilist(patientid)


    Column(modifier = modifier.fillMaxSize().background(AppBg),
        verticalArrangement = Arrangement.Bottom){
        val list =  viewmodel.patientmedilist.collectAsState()

        Text("Medicines" , fontFamily = Headingfont , fontSize = 26.sp, modifier = Modifier.padding(start = 12.dp) , color = Color.White )

        Box(contentAlignment = Alignment.BottomEnd) {
            LazyColumn(modifier = Modifier.padding(top = 12.dp).fillMaxWidth().fillMaxHeight(0.7f) , contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(list.value){
                    medi  -> MediCard(medi)
                }
            }

            FloatingActionButton( onClick = {
                navcontroller.navigate(Destinations.AddMediceneScreen.ref.replace(oldValue = "{patientid}" , newValue = patientid))
            },
                modifier = Modifier.padding(end = 18.dp , bottom = 18.dp),
                shape = CircleShape,
                containerColor = HTextClr
            ){
                Icon( imageVector = Icons.Filled.Add , contentDescription = "addpill",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }


    }


    // Display a Vitals Dash , take in the vitals from the user and the display it in a dashboard format

    // Allow the Nurse to set the medication of this current patient 

}
//
@Composable
fun MediCard(medicine : MedieneInfo){
    Card(colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier.padding(bottom = 12.dp)
            .border(0.1.dp, color = Color.White.copy(alpha = 0.2f) , shape = CardDefaults.shape)
    ){
        Row(modifier = Modifier.padding(16.dp).fillMaxSize() , verticalAlignment = Alignment.CenterVertically) {

            // If else Statements
            if(medicine.medi_type == MediTypeState.tablet.ref){
                Image(imageVector = ImageVector.vectorResource(R.drawable.tablet2) , contentDescription = "" ,
                    modifier = Modifier.size(65.dp)
                )
            }
            else if(medicine.medi_type == MediTypeState.Capsule.ref){
                Image(imageVector = ImageVector.vectorResource(R.drawable.capsule1) , contentDescription = "" ,
                    modifier = Modifier.size(65.dp)
                )
            }
            else if(medicine.medi_type == MediTypeState.Tonic.ref){
                Image(imageVector = ImageVector.vectorResource(R.drawable.tonic2) , contentDescription = "" ,
                    modifier = Modifier.size(65.dp)
                )
            }
            else if(medicine.medi_type == MediTypeState.Drops.ref){
                Image(imageVector = ImageVector.vectorResource(R.drawable.drops) , contentDescription = "" ,
                    modifier = Modifier.size(65.dp)
                )
            }
            else if(medicine.medi_type == MediTypeState.Injection.ref){
                Image(imageVector = ImageVector.vectorResource(R.drawable.syring1) , contentDescription = "" ,
                    modifier = Modifier.size(65.dp)
                )
            }
            else{
                Image(imageVector = ImageVector.vectorResource(R.drawable.medkit) , contentDescription = "" ,
                    modifier = Modifier.size(65.dp)
                )
            }
            Spacer(modifier = Modifier.size(18.dp))

            Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically) {


                // GET THE UPCOMING DOSE , AND IF PREVIOUS DOSAGES WHERE MISSED INDICATE MISSED DOSAGES
                Column(modifier = Modifier,
                    verticalArrangement = Arrangement.Center) {
                    Text("${medicine.medi_name}" , color = Color.White , fontSize = 20.sp , fontFamily = Headingfont)
                    Text("Upcoming : ${medicine.meditime}" , fontSize = 16.sp , fontFamily = Bodyfont , color = Color.White  )
                }

                Icon(imageVector = ImageVector.vectorResource(R.drawable.gomedi) , contentDescription = "" ,
                    modifier = Modifier.size(35.dp)
                )

            }
        }
    }

}