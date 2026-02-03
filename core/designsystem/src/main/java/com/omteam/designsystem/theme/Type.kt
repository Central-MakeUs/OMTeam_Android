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
    Font(R.font.paperlogy_thin, FontWeight.Thin),     // 100
    Font(R.font.paperlogy_extralight, FontWeight.ExtraLight),   // 200
    Font(R.font.paperlogy_light, FontWeight.Light),             // 300
    Font(R.font.paperlogy_regular, FontWeight.Normal),          // 400
    Font(R.font.paperlogy_medium, FontWeight.Medium),           // 500
    Font(R.font.paperlogy_semibold, FontWeight.SemiBold),       // 600
    Font(R.font.paperlogy_bold, FontWeight.Bold),               // 700
    Font(R.font.paperlogy_extrabold, FontWeight.ExtraBold),     // 800
    Font(R.font.paperlogy_black, FontWeight.Black)              // 900
)

/**
 * 사용 방법
 *
 *
 * ```kotlin
 * Button(
 *     onClick = { /* ... */ },
 *     enabled = isEnabled
 * ) {
 *     Text(
 *         text = "버튼",
 *         style = if (isEnabled) {
 *             PretendardType.button01Enabled
 *         } else {
 *             PretendardType.button01Disabled
 *         }
 *     )
 * }
 * ```
 */
object PaperlogyType {
    // Headline
    val headline01 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 24.sp,  // 100%
        letterSpacing = 0.sp
    )

    val headline02 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 33.sp,  // 150%
        letterSpacing = 0.sp
    )

    val headline02_2 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 22.sp,  // 100%
        letterSpacing = 0.sp
    )

    val headline03 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 20.sp,  // 100%
        letterSpacing = 0.sp
    )

    val headline04 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 18.sp,  // 100%
        letterSpacing = 0.sp
    )

    // Button
    val button01 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 18.sp,  // 100%
        letterSpacing = 0.sp
    )

    val button02 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 18.sp,  // 100%
        letterSpacing = 0.sp
    )

    val onboardingCardText = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 20.sp,  // 100%
        letterSpacing = 0.sp
    )

    // Body
    val body01 = TextStyle(
        fontFamily = PaperlogyFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 16.sp,  // 100%
        letterSpacing = 0.sp
    )
}

// 서브 폰트
val PretendardFont = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),    // 100
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),  // 200
    Font(R.font.pretendard_light, FontWeight.Light),            // 300
    Font(R.font.pretendard_regular, FontWeight.Normal),         // 400
    Font(R.font.pretendard_medium, FontWeight.Medium),          // 500
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),      // 600
    Font(R.font.pretendard_bold, FontWeight.Bold),              // 700
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),    // 800
    Font(R.font.pretendard_black, FontWeight.Black)             // 900
)

object PretendardType {
    // Body
    val body01 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal, // 400
        lineHeight = 18.sp,  // 100%
        letterSpacing = (-0.072).sp
    )

    val body02 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 15.sp,  // 100%
        letterSpacing = (-0.4).sp
    )

    val body02_2 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 22.4.sp,  // 140% (16px × 1.4 = 22.4px)
        letterSpacing = (-0.4).sp
    )

    val body04_1 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 12.sp,  // 100%
        letterSpacing = (-0.048).sp
    )

    val body04_2 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 12.sp,  // 100%
        letterSpacing = (-0.048).sp
    )

    val body04_3 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 15.6.sp,  // 130% (12px × 1.3 = 15.6px)
        letterSpacing = (-0.048).sp
    )

    val body05 = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 15.sp,  // 100%
        letterSpacing = (-0.06).sp
    )

    // Button
    val button01Disabled = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 18.sp,  // 100%
        letterSpacing = (-0.072).sp
    )

    val button01Enabled = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 18.sp,  // 100%
        letterSpacing = 0.sp
    )

    val button02Disabled = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 16.sp,  // 100%
        letterSpacing = (-0.064).sp
    )

    val button02Enabled = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 16.sp,  // 100%
        letterSpacing = (-0.064).sp
    )

    val button03Disabled = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal, // 400
        lineHeight = 15.sp,  // 100%
        letterSpacing = (-0.06).sp
    )

    val button03Abled = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 14.sp,  // 100%
        letterSpacing = (-0.056).sp
    )

    val skipButton = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 14.sp,  // 100%
        letterSpacing = (-0.056).sp
    )

    val homeLevelText = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold, // 600
        lineHeight = 13.sp,  // 100%
        letterSpacing = (-0.052).sp
    )

    val tabLabel = TextStyle(
        fontFamily = PretendardFont,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium, // 500
        lineHeight = 11.sp,  // 100%
        letterSpacing = (-0.044).sp
    )
}

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