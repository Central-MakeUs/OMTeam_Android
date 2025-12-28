package com.omteam.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.omteam.omt.core.designsystem.R

// 메인 폰트
val PaperlogyFont = FontFamily(
    Font(R.font.paperlogy_thin, FontWeight.Thin),           // 100
    Font(R.font.paperlogy_extralight, FontWeight.ExtraLight),   // 200
    Font(R.font.paperlogy_light, FontWeight.Light),             // 300
    Font(R.font.paperlogy_regular, FontWeight.Normal),          // 400
    Font(R.font.paperlogy_medium, FontWeight.Medium),           // 500
    Font(R.font.paperlogy_semibold, FontWeight.SemiBold),       // 600
    Font(R.font.paperlogy_bold, FontWeight.Bold),               // 700
    Font(R.font.paperlogy_extrabold, FontWeight.ExtraBold),     // 800
    Font(R.font.paperlogy_black, FontWeight.Black)              // 900
)

// 서브 폰트
val PretendardFont = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),           // 100
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),  // 200
    Font(R.font.pretendard_light, FontWeight.Light),            // 300
    Font(R.font.pretendard_regular, FontWeight.Normal),         // 400
    Font(R.font.pretendard_medium, FontWeight.Medium),          // 500
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),      // 600
    Font(R.font.pretendard_bold, FontWeight.Bold),              // 700
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),    // 800
    Font(R.font.pretendard_black, FontWeight.Black)             // 900
)

/**
 * MaterialTheme.typography 통해 접근
 *
 * ```kotlin
 * Text(
 *     text = "메인 폰트",
 *     style = MaterialTheme.typography.bodyLarge  // paperlogy 적용됨
 * )
 * ```
 *
 * 직접 명시해서 사용
 *
 * ```kotlin
 * Text(
 *     text = "서브 폰트",
 *     fontFamily = PretendardFont,
 *     fontWeight = FontWeight.Medium
 * )
 * ```
 *
 */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PaperlogyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PaperlogyFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PaperlogyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)