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
import androidx.compose.ui.res.stringResource
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.R

@Composable
fun SetSimilarPatternScreen(
    onPatternChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedPattern by remember { mutableStateOf("") }

    val patternOptions = listOf(
        stringResource(R.string.pattern_first),
        stringResource(R.string.pattern_second),
        stringResource(R.string.pattern_third),
        stringResource(R.string.pattern_fourth),
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
                    text = stringResource(R.string.set_similar_pattern_screen_title),
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
                    text = stringResource(R.string.before),
                    onClick = { onBack() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = GreenSub03Button,
                    textColor = GreenSub07Button,
                    modifier = Modifier.width(dp126)
                )

                Spacer(modifier = Modifier.width(dp9))

                OMTeamButton(
                    text = stringResource(R.string.next),
                    onClick = { onNext() },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = if (selectedPattern.isNotEmpty()) Green07 else Green04,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }
}