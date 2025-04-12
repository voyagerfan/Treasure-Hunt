package com.example.treasurehunt.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.treasurehunt.R

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

val catamaranFamily = FontFamily(
    Font(R.font.catamaran_regular),
    Font(R.font.catamaran_black),
    Font(R.font.catamaran_light),
    Font(R.font.catamaran_extra_light),
    Font(R.font.catamaran_thin),
    Font(R.font.catamaran_medium),
    Font(R.font.catamaran_bold),
    Font(R.font.catamaran_semi_bold),
    Font(R.font.catamaran_extra_bold),
    Font(R.font.catamaran_variable_font_wght)
)
