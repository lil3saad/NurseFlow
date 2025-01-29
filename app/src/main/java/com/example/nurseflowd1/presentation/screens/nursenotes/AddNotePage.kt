package com.example.nurseflowd1.presentation.screens.nursenotes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.datalayer.datamodel.NurseNote
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.presentation.AppBarColorState
import com.example.nurseflowd1.presentation.AppBarTitleState
import com.example.nurseflowd1.presentation.BottomBarState
import com.example.nurseflowd1.presentation.NavigationIconState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.Headingfont



@Composable
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7A)
fun MyPreview(){

}
@Composable
fun AddNurseNote(modifier : Modifier , viewmodel : AppVM , navcontroller : NavController ,
                 isUpdatePage : Boolean ,
                 noteid : Long ,
                 notetitle : String , notebody : String  ){

    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("New Note"),
        colorState = AppBarColorState.DefaultColors,
        iconState = NavigationIconState.DefaultBack
    )
    viewmodel.ChangeBottomBarState(
        barstate = BottomBarState.NoBottomBar
    )
    Log.d("NOTEUPDATE", " PASSED FROM NOTES PAGE $isUpdatePage")
    Column(modifier = Modifier.fillMaxSize().background(AppBg) , horizontalAlignment = Alignment.CenterHorizontally){

        val user_title = remember { mutableStateOf(notetitle) }
        val user_body = remember { mutableStateOf(notebody) }

        NoteCanvas(modifier, user_title, user_body)

        ButtonRow(user_title , user_body, isUpdatePage , onSave = { note ->
             viewmodel.CreateNote(note)
             navcontroller.popBackStack() },
            onUpdate = { note ->
                viewmodel.UpdateNote(NurseNote(noteid = noteid , title = note.title , body = note.body)  )
                navcontroller.popBackStack()
            },
            onCancel = { navcontroller.popBackStack() }
        )

    }

}
@Composable
fun NoteCanvas(modifier: Modifier, title : MutableState<String> , body : MutableState<String>) {
    Column(modifier = modifier.fillMaxWidth().fillMaxHeight(0.8f).padding(horizontal = 20.dp, vertical = 20.dp)
        .border(1.dp , Color.Black, shape = RoundedCornerShape(50.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(value = title.value , onValueChange = { usertext -> title.value = usertext },
            placeholder = { Text("Enter Title Here..." , fontSize = 28.sp , fontFamily = Headingfont , color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp , start = 8.dp , end = 8.dp),
            textStyle = TextStyle(fontSize = 32.sp , fontFamily = Headingfont , textAlign = TextAlign.Left),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
        TextField(value = body.value , onValueChange = { usertext -> body.value = usertext },
            placeholder = { Text("Body Here....." , fontSize = 18.sp , fontFamily = Bodyfont , color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().fillMaxSize().padding(top = 20.dp , start = 8.dp , end = 8.dp),
            textStyle = TextStyle(fontSize = 32.sp , fontFamily = Headingfont , textAlign = TextAlign.Left),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
    }
}
@Composable
fun ButtonRow(title : MutableState<String> , body : MutableState<String> , isupdate : Boolean ,
              onSave : (note : NurseNote) -> Unit, onUpdate : (note : NurseNote) -> Unit ,
              onCancel : () -> Unit
){
    Row(modifier = Modifier.fillMaxWidth(0.8f) , horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { onCancel() }) {
            Text("Cancel")
        }
        Button(onClick = {
            var isvaild = true
            if (title.value.isNullOrBlank()) isvaild = false

            if(body.value.isNullOrBlank()) isvaild = false

            if(isvaild) {
                if(isupdate){
                    val note = NurseNote(noteid = 0L, title = title.value.trim(), body = body.value.trim())
                    onUpdate(note)
                }else {
                    val note = NurseNote(noteid = kotlin.random.Random.nextLong(0, Long.MAX_VALUE ) , title = title.value.trim(), body = body.value.trim())
                    onSave(note) }
            }
          }
        ){

            Text("Save")
        }

    }
}
