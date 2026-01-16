package com.omteam.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp12
import com.omteam.designsystem.foundation.dp126
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp200
import com.omteam.designsystem.foundation.dp60
import com.omteam.designsystem.foundation.dp8
import com.omteam.designsystem.foundation.dp9
import com.omteam.designsystem.theme.GreenSub03Button
import com.omteam.designsystem.theme.GreenSub07Button
import com.omteam.designsystem.theme.PaperlogyType

@Composable
fun SetSimilarPatternScreen(
    onPatternChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedPattern by remember { mutableStateOf("") }

    val patternOptions = listOf(
        "비교적 규칙적인 평일 주간 근무에요.",
        "야근/불규칙한 일정이 자주 있어요.",
        "주기적으로 교대/밤샘 근무가 있어요.",
        "일정이 매일매일 달라요."
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OMTeamText(
                    text = "최근 한 달 간의 생활패턴과\n가장 유사한 것을 선택해주세요.",
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp20))

                patternOptions.forEachIndexed { index, time ->
                    OMTeamCard(
                        text = time,
                        isSelected = (selectedPattern == time),
                        textStyle = PaperlogyType.onboardingCardText,
                        onClick = {
                            // 같은 카드 짝수 회 클릭 시 선택 해제
                            selectedPattern = if (selectedPattern == time) {
                                ""
                            } else {
                                time
                            }
                            onPatternChange(selectedPattern)
                        }
                    )

                    if (index < patternOptions.size - 1) {
                        Spacer(modifier = Modifier.height(dp12))
                    }
                }

                // 버튼과 안 겹치게 여백 추가
                Spacer(modifier = Modifier.height(dp20))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dp20),
                horizontalArrangement = Arrangement.Center,
            ) {
                OMTeamButton(
                    text = "이전",
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = "다음",
                    onClick = { onNext() },
                    height = dp60,
                    cornerRadius = dp8,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }
}