package com.omteam.impl.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.designsystem.theme.PretendardType
import com.omteam.impl.screen.component.OnboardingStepIndicator

@Composable
fun OnboardingScreen(
    currentStep: Int,
    totalSteps: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // SKIP 버튼만 우측 상단에 배치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dp24,
                    end = dp16,
                ),
            horizontalArrangement = Arrangement.End,
        ) {
            OMTeamText(
                text = "SKIP",
                style = PretendardType.skipButton,
                color = GreenSub06,
                modifier = Modifier
                    .background(
                        color = GreenSub02,
                        shape = RoundedCornerShape(dp32)
                    )
                    .border(
                        width = dp1,
                        color = GreenSub05,
                        shape = RoundedCornerShape(dp32)
                    )
                    .clickable { onSkip() } // 메인 화면 이동
                    .padding( // 텍스트 주변 여백
                        horizontal = dp20,
                        vertical = dp8
                    )
            )
        }

        Spacer(modifier = Modifier.height(dp38))
        OnboardingStepIndicator(currentStep = currentStep)
        Spacer(modifier = Modifier.height(dp48))

        // 나머지 온보딩 컨텐츠
        // 좌우 20dp씩 떨어져서 표시돼야 함
        when (currentStep) {
            // 1 ->
        }
        Text("온보딩 화면 1부터 시작")
    }
}