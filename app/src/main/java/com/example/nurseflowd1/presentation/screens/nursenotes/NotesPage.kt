package com.example.nurseflowd1.presentation.screens.nursenotes

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.presentation.AppBarColorState
import com.example.nurseflowd1.presentation.AppBarTitleState
import com.example.nurseflowd1.presentation.NavigationIconState
import com.example.nurseflowd1.ui.theme.AppBg
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.nurseflowd1.datalayer.datamodel.NurseNote
import com.example.nurseflowd1.presentation.BottomBarState
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.Headingfont
import com.example.nurseflowd1.ui.theme.SecClr
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.nurseflowd1.presentation.Destinations
import com.example.nurseflowd1.ui.theme.panelcolor

@Preview(showSystemUi = true, device = Devices.PIXEL_7A, showBackground = true)
@Composable
fun Preview() {

}
@Composable
fun NurseNotesPage(modifier: Modifier , navController: NavController, viewmodel : AppVM){
    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("Notes"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.None
    )
    viewmodel.ChangeBottomBarState(BottomBarState.NotesPage)
    LaunchedEffect(Unit) {
        viewmodel.getNoteList()
    }
    val NoteList by viewmodel.notelist.collectAsState()
    Column(modifier = modifier.background(AppBg)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        val searchtext = remember { mutableStateOf("") }
        NoteSearchRow(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp), searchtext , viewmodel)

        when(val state = NoteList){
                is RoomNoteListState.Receive -> {
                    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(150.dp) ) {
                        items(state.list) { note ->
                            NoteCard(note,
                                onDelete = { note ->
                                    // Dialog
                                    viewmodel.DeleteNote(note)
                                },
                                onUpdate = { updatenote ->
                                    val route = Destinations.AddNoteScreen.createRoute(
                                        isUpdate = true,
                                        noteId = updatenote.noteid,
                                        noteTitle = updatenote.title,
                                        noteBody = updatenote.body
                                    )
                                    Log.d(
                                        "NOTEUPDATE",
                                        "Route: $route"
                                    )  // Add this to see the generated route
                                    navController.navigate(route)
                                },
                                navController
                            )
                        }
                    }
                }
                is RoomNoteListState.EmptyList -> {
                        Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center) {
                            Text("Please Add Notes, no notes found" , style = TextStyle(fontSize = 15.sp , fontFamily = Bodyfont , color = Color.Black))
                        }
                }
                is RoomNoteListState.Loading -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator( modifier = Modifier.size(200.dp ) , color = panelcolor)
                        }
                }
                else -> Unit
            }
    }

}
@Composable
fun NoteCard(note : NurseNote = NurseNote(1, "ThisNote1" , "this is the body of teh note i want to be display way , this is how tkshdlf sadjf lksdjf klsadjf skdljf sdaoifu dslfkjsd fkljsdfk ljsdf klsdjf ksdjf ksdfj asdkfjds fk;jds fk;sdjf sdif dsfs  this is the how this adkfl jsdklfj sdklfj sdklf jsdkl;fjdas fklsdjf kl;dsjf sd jthis sfhidf lksdjf ksdljfdsofj dskfj lkfjd fjdsklf jsdklf sdjfksdjfsdk;f jsdkfj sdkfjsd;kf jds f;dsj d;fjsdkfl j  this his this hafljd fkjsdfk jfk djfsdjf sd;kldfj sdpfoj weir jekrjwekr jsdfsdf sdf jdsd ds sd  ") ,
              onDelete : (note : NurseNote) -> Unit,
              onUpdate : (note : NurseNote) -> Unit,
             navcontroller : NavController
){

    Card( modifier = Modifier.padding(12.dp) ,
        colors = CardDefaults.cardColors(
            containerColor = SecClr),
        border = BorderStroke(0.2.dp, Color.Black.copy(alpha = 0.1f))
    ){
        Row(modifier = Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.Bottom ) {
            Text("${note.title}"  , style = TextStyle(fontSize = 25.sp , color = Color.Black  , fontFamily = Headingfont) , modifier = Modifier.width(120.dp).padding( start = 12.dp , top = 12.dp , bottom = 6.dp)  )

            Row(modifier = Modifier.padding( horizontal = 12.dp , vertical = 6.dp) ) {
                Icon(imageVector  = Icons.Default.Create , contentDescription = "Edit Icon" , tint = Color.DarkGray ,
                        modifier = Modifier.size(20.dp)
                            .clickable{
                                  onUpdate(note)
                            }
                )
                Spacer(modifier = Modifier.size(15.dp))
                Icon(imageVector  = Icons.Default.Delete , contentDescription = "Delete Icon"  , tint = Color.DarkGray,  modifier = Modifier.size(20.dp)
                    .clickable{
                           onDelete(note)
                    }
                )
            }

        }

        Column(horizontalAlignment = Alignment.CenterHorizontally , modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f) , color = Color.DarkGray , thickness = 0.5.dp)
                Text("${note.body}", style = TextStyle(fontSize = 15.sp , color = Color.DarkGray , fontFamily = Bodyfont) ,
                    maxLines = 15 , modifier = Modifier.padding(12.dp).clickable{
                        onUpdate(note)
                    }
                )
        }
    }


}


@Composable
fun NoteSearchRow(modifier: Modifier , Searchtext : MutableState<String> , viewmodel: AppVM ) {
    var isSearch by remember { mutableStateOf(true) }
    val softwarekeybaord = LocalSoftwareKeyboardController.current!!

    Row(modifier = modifier.fillMaxWidth(0.85f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField( value = Searchtext.value ,
            onValueChange = { usertext -> Searchtext.value = usertext },
            modifier = Modifier
                .fillMaxWidth()
                .height( 55.dp ),
            placeholder = { Text("Search note title" , color = Color.DarkGray  , fontSize = 12.sp)
            } ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Black.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                cursorColor = panelcolor
            ),
            trailingIcon = {
                if(isSearch){
                    Icon( imageVector = Icons.Default.Search , contentDescription = "Search Patients" , modifier = Modifier
                        .padding(end = 12.dp)
                        .size( 38.dp ).clickable{
                            isSearch = false
                            val username = "%${Searchtext.value}%"
                            viewmodel.getSearchList(username)
                            softwarekeybaord.hide()
                        } ,
                        tint = Color.DarkGray)
                }else {
                    Icon( imageVector = Icons.Default.Close , contentDescription = "Search Patiens" , modifier = Modifier
                        .padding(end = 12.dp)
                        .size( 38.dp )
                        .clickable{
                           viewmodel.getNoteList()
                            isSearch = true
                            Searchtext.value = ""
                        },
                        tint = Color.DarkGray)
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
                    viewmodel.getSearchList(username)
                    softwarekeybaord.hide()
                }
            )
        )

    }
}