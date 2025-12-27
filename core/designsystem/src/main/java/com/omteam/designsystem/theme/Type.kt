package com.omteam.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.omteam.omt.core.designsystem.R

val PretendardFont = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),  // 100
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),// 200
    Font(R.font.pretendard_light, FontWeight.Light),          // 300
    Font(R.font.pretendard_regular, FontWeight.Normal),       // 400
    Font(R.font.pretendard_medium, FontWeight.Medium),        // 500
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),    // 600
    Font(R.font.pretendard_bold, FontWeight.Bold),            // 700
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),  // 800
    Font(R.font.pretendard_black, FontWeight.Black)           // 900
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)