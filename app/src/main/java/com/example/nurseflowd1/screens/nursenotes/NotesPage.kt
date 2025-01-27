package com.example.nurseflowd1.screens.nursenotes

import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.Headingfont
import com.example.nurseflowd1.ui.theme.panelcolor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.text.font.Typeface
import com.example.nurseflowd1.R

@Preview(showSystemUi = true, device = Devices.PIXEL_7A, showBackground = true)
@Composable
fun Preview(){
     WavePanel()
}
@Composable
fun NurseNotesPage(modifier: Modifier , navController: NavController, viewmodel : AppVM){
    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("Notes"),
        colorState = AppBarColorState.DefaultColors,
        NavigationIconState.None
    )
    viewmodel.ChangeBottomBarState(BottomBarState.NotesPage)
    Column(modifier = modifier
        .background(AppBg)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("THIS IS NURSE NOTES PAGE" , color =  Color.DarkGray )
        WavePanel()
    }
}


@Composable
fun InstagramLogo(){
    Canvas(modifier = Modifier
        .size(400.dp)
        .border(1.dp, color = Color.Black)
        .background(AppBg)
        .padding(22.dp)) {
        val canvasWidth =  size.width
        val canvasHeight = size.height

        drawRoundRect(brush = Brush.linearGradient(
          colors = listOf<Color>(Color.Yellow , Color.Red, Color.Magenta)),
            style = Stroke(60f),
            cornerRadius = CornerRadius(100.dp.toPx(), 100.dp.toPx())
        )
        drawCircle( brush = Brush.linearGradient(
            colors = listOf<Color>(Color.Red, Color.Yellow)),
            radius = 200f,
            style = Stroke(65f)
        )
        drawCircle( brush = Brush.linearGradient(
            colors = listOf<Color>(Color.Red, Color.Red.copy(alpha = 0.5f))),
                 radius = 75f,
                 center = Offset( x = canvasWidth * 0.8f , y = canvasHeight * 0.2f)
            )
    }
}
@Composable
fun WavePanel(){


    val icon = rememberVectorPainter( image = ImageVector.vectorResource(R.drawable.medkit))

    Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(1.dp, color = Color.Black)
            .background(AppBg)) {



            val canvasWidth =  size.width
            val canvasHeight = size.height

            val firstpath = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f,0f)
                lineTo(canvasWidth, 0f)
                quadraticTo(
                    canvasWidth * 0.85f, canvasHeight * 0.15f,
                    canvasWidth * 0.55f, canvasHeight * 0.3f
                )
                quadraticTo(
                    canvasWidth * 0.35f, canvasHeight * 0.1f,
                    canvasWidth * 0f, canvasHeight * 0.1f,
                )
                close()
            }
            drawPath(path = firstpath, brush = Brush.linearGradient(
                colors = listOf(Color.Red, Color.Magenta) )
            )
            val secondpath = androidx.compose.ui.graphics.Path().apply {
                moveTo(canvasWidth,0f)
                quadraticTo(
                    canvasWidth * 0.85f, canvasHeight * 0.15f,
                    canvasWidth * 0.55f, canvasHeight * 0.3f
                )
                quadraticTo(
                    canvasWidth * 0.35f, canvasHeight * 0.1f,
                    canvasWidth * 0f, canvasHeight * 0.1f,
                )
                lineTo(0f, canvasHeight * 0.3f)
                cubicTo(
                    canvasWidth * 0.2f, canvasHeight * 0.1f,
                    canvasWidth * 0.55f, canvasHeight * 0.3f,
                    canvasWidth * 0.85f, canvasHeight * 0.45f,
                )
                quadraticTo(
                    canvasWidth * 0.99f, canvasHeight * 0.35f,
                    canvasWidth , canvasHeight * 0.25f
                )
            }
            drawPath(path = secondpath, brush = Brush.linearGradient(
                colors = listOf(Color.Yellow, Color.Magenta)))
            val thridpath = androidx.compose.ui.graphics.Path().apply {
                moveTo( 0f, canvasHeight * 0.3f)
                cubicTo(
                    canvasWidth * 0.3f, canvasHeight * 0.03f,
                    canvasWidth * 0.69f, canvasHeight * 0.46f,
                    canvasWidth , canvasHeight * 0.45f,
                )
                cubicTo(
                    canvasWidth * 0.8f, canvasHeight * 0.65f,
                    canvasWidth * 0.45f, canvasHeight * 0.25f,
                    0f , canvasHeight * 0.5f,
                )
                close()
            }
            drawPath(path = thridpath, brush = Brush.linearGradient(
                colors = listOf(Color.Red, Color.Magenta))
            )
            val smallpth = androidx.compose.ui.graphics.Path().apply {
                moveTo( canvasWidth , canvasHeight * 0.45f )
                lineTo( canvasWidth , canvasHeight * 0.26f)
                quadraticTo(
                    canvasWidth * 0.98f, canvasHeight * 0.35f,
                    canvasWidth * 0.86f , canvasHeight * 0.43f
                )
                quadraticTo(
                    canvasWidth * 0.86f , canvasHeight * 0.45f,
                    canvasWidth , canvasHeight * 0.45f
                )
            }
            drawPath(path = smallpth, brush = Brush.linearGradient(
                colors = listOf(Color.Magenta, Color.Red))
            )

        translate(left = 850f , top = 150f){
            with(icon){
                draw(size = Size(width = 150f , height = 150f))
            }
        }


        val textpaint = android.graphics.Paint().apply {
            textSize = 42f
            color = android.graphics.Color.WHITE
        }
        drawContext.canvas.nativeCanvas.drawText(
            "Good Morning",
            center.x - 450f,
            center.y - 200f,
            textpaint
        )
        val textpaint2 = android.graphics.Paint().apply {
            textSize = 62f
            color = android.graphics.Color.WHITE
        }
        drawContext.canvas.nativeCanvas.drawText(
            "Syed Saad",
            center.x - 420f,
            center.y - 120f,
            textpaint2
        )
    }
}

@Composable
fun FacebookLogo(){
    val textmeasure = rememberTextMeasurer()
        val text = "f"
    val textlayoutmeasure = remember(text) {
        textmeasure.measure(text)
    }
    Canvas(modifier = Modifier
        .size(400.dp)
        .border(1.dp, color = Color.Black)
        .background(AppBg)
        .padding(22.dp)) {
        val canvasWidth =  size.width
        val canvasHeight = size.height

         drawRoundRect(color = panelcolor,
             cornerRadius = CornerRadius(x = 35.dp.toPx() , 35.dp.toPx())
         )

       // TF is Paint ?
        val paint  = android.graphics.Paint().apply {
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 782f
            color = Color.White.toArgb()
        }
        // What is Native Canvas
        drawContext.canvas.nativeCanvas.drawText(
             "f",
             center.x+300f,
             center.y+500f,
            paint as android.graphics.Paint
        )



    }
}

@Composable
fun MessengerLogo(){
    Canvas(modifier = Modifier
        .size(400.dp)
        .border(1.dp, color = Color.Black)
        .background(AppBg)
        .padding(22.dp)) {

             val canvasWidth = size.width
             val canvasHeight = size.height
        drawOval(color = panelcolor,
            size = Size(width = canvasWidth , height = canvasHeight * 0.85f)
        )
        val TriganglePath  = androidx.compose.ui.graphics.Path().apply {
            moveTo(canvasWidth * 0.2f , canvasHeight * 0.7f)
            lineTo(canvasWidth * 0.05f , canvasHeight * 0.9f)
            lineTo(canvasWidth * 0.3f , canvasHeight * 0.8f)
            close()
        }
        drawPath(path = TriganglePath,
            color = panelcolor
        )

        val LighthingPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(canvasWidth * 0.15f , canvasHeight * 0.55f)
            lineTo(canvasWidth * 0.35f , canvasHeight * 0.3f)
            lineTo(canvasWidth * 0.55f , canvasHeight * 0.4f)
            lineTo(canvasWidth * 0.85f , canvasHeight * 0.3f)
            lineTo(canvasWidth * 0.55f , canvasHeight * 0.55f)
            lineTo(canvasWidth * 0.35f , canvasHeight * 0.42f)
            close()
        }
        drawPath(path = LighthingPath,
            color = Color.White
        )
    }
}

@Composable
fun AppleCloud(){
    Canvas(modifier = Modifier
        .size(400.dp)
        .border(1.dp, color = Color.Black)
        .background(AppBg)
        .padding(22.dp)) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRoundRect(
            color = panelcolor,
            cornerRadius = CornerRadius(x = 122f , y = 122f)
        )
        drawCircle(color = Color.Yellow ,
             radius = 145f,
            center = Offset(x = canvasWidth * 0.35f  , y = canvasHeight * 0.40f )
        )
        val Cloudpath = androidx.compose.ui.graphics.Path().apply {
            moveTo( canvasWidth.times(0.35f) , canvasHeight.times(0.75f) )
            cubicTo(
                canvasWidth.times(0.20f) , canvasHeight.times(0.65f),
                canvasWidth.times(0.20f) , canvasHeight.times(0.55f),
                canvasWidth.times(0.35f) , canvasHeight.times(0.50f),
            )
            cubicTo(
                canvasWidth.times(0.30f) , canvasHeight.times(0.4f),
                canvasWidth.times(0.55f) , canvasHeight.times(0.2f),
                canvasWidth.times(0.65f) , canvasHeight.times(0.45f),
            )
            cubicTo(
                canvasWidth.times(0.85f) , canvasHeight.times(0.40f),
                canvasWidth.times(0.95f) , canvasHeight.times(0.78f),
                canvasWidth.times(0.7f) , canvasHeight.times(0.75f),
            )
            close()
        }
        drawPath(path = Cloudpath,
            color = Color.White.copy(alpha = 0.95f),
        )
    }
}

// Even Tho I have put a fixed canvas size , the drawtext adjusts itself not to the dynamic canvas size allocated by something else in general and is different for devies with different dimensions