package com.example.nurseflowd1.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.R
import com.example.nurseflowd1.domain.AppVM


@Composable
@Preview( showBackground = true , showSystemUi = true , device = Devices.PIXEL_7A)
fun preview(){
    setpreview()
}
@Composable
fun setpreview(){

}
@Composable
fun AccountScreen( modifier : Modifier = Modifier, viewmodel : AppVM = AppVM() , navcontroller: NavController){
    Column( modifier = modifier.fillMaxSize().background(AppBg),
        horizontalAlignment = Alignment.End
    ){
        Button(onClick = {
            viewmodel.SingOut()
            navcontroller.popBackStack(route = Destinations.NurseDboardScreen.ref , inclusive = false)
        }) {
            Text( text = "SignOut" , style = TextStyle(fontSize = 22.sp))
        }
//        Row( modifier = Modifier.weight(0.2f).fillMaxWidth().background( color = Color.White) ,
//            horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.CenterVertically
//        ){
//            Image( painter = painterResource(R.drawable.syringe), contentDescription = "AccountPicture" ,
//                modifier = Modifier.border(color = Color.Black , width = 1.dp)
//            )
//        }
//        Column(modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Red)
//        ){
//            Text( text = "Name : " , style = TextStyle(fontSize = 22.sp))
//            Row {
//                Text( text = "Gender " , style = TextStyle(fontSize = 22.sp))
//                Text( text = "Age: " , style = TextStyle(fontSize = 22.sp))
//            }
//            Text( text = "Hospital Name " , style = TextStyle(fontSize = 22.sp))
//            Text( text = "Hospital Id : " , style = TextStyle(fontSize = 22.sp))
//            Text( text = "Nurse License Id: " , style = TextStyle(fontSize = 22.sp))
//            Text( text = "Email  Id: " , style = TextStyle(fontSize = 22.sp))
//            Text( text = "Phone no" , style = TextStyle(fontSize = 22.sp))
//        }
    }
}