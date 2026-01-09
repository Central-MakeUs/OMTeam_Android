package com.omteam.impl.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.designsystem.theme.PretendardType

@Composable
fun OnboardingStepIndicator(
    currentStep: Int,
    totalSteps: Int = 7,
    modifier: Modifier = Modifier,
) {
    // 가로선
    Box(
        modifier = modifier
            .fillMaxWidth()
            // 자식 요소(원)들이 그려지기 전에 배경 먼저 그림
            .drawBehind {
                drawLine(
                    color = GreenSub05,
                    start = Offset(0f, size.height / 2), // 왼쪽 끝, 세로 중앙
                    end = Offset(size.width, size.height / 2), // 오른쪽 끝, 세로 중앙
                    strokeWidth = dp1.toPx()
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // 온보딩 단계(원) 컨테이너
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp16)
        ) {
            for (step in 1 .. totalSteps) {
                StepCircle(
                    stepNumber = step,
                    isActive = (step == currentStep)
                )
            }
        }
    }
}

@Composable
private fun StepCircle(
    stepNumber: Int,
    isActive: Boolean
) {
    Box(
        modifier = Modifier
            .size(dp40)
            .background(
                color = if (isActive) Green07 else GreenSub04,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        OMTeamText(
            text = String.format("%02d", stepNumber),
            color = if (isActive) Color.White else GreenSub01,
            style = PretendardType.button03Disabled
        )
    }
}