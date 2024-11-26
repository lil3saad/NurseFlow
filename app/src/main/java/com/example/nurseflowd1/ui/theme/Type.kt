package com.example.nurseflowd1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.nurseflowd1.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
val Headingfont = FontFamily (
    listOf(
        Font( R.font.generalsans , weight = FontWeight.Normal),
        Font(R.font.generalsans, weight = FontWeight.Bold),
        Font(R.font.generalsans , weight = FontWeight.Thin),
        Font(R.font.generalsans, weight = FontWeight.Medium),
        Font(R.font.generalsans, weight = FontWeight.ExtraBold)
    )
)
val Bodyfont  = FontFamily (
    listOf(
        Font( R.font.nunito , weight = FontWeight.Normal),
        Font(R.font.nunito , weight = FontWeight.Bold),
        Font(R.font.nunito , weight = FontWeight.Thin),
        Font(R.font.nunito, weight = FontWeight.Medium),
        Font(R.font.nunito, weight = FontWeight.ExtraBold)
    )
)
