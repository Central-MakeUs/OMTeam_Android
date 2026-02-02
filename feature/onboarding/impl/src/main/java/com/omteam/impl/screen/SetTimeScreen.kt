package com.omteam.impl.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.card.OMTeamCard
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.R

@Composable
fun SetTimeScreen(
    onTimeChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    var selectedTime by remember { mutableStateOf("") }

    val timeOptions = listOf(
        stringResource(R.string.time_first),
        stringResource(R.string.time_second),
        stringResource(R.string.time_third),
        stringResource(R.string.time_fourth),
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
                    text = stringResource(R.string.set_time_screen_title),
                    style = PaperlogyType.headline02
                )

                Spacer(modifier = Modifier.height(dp20))

                timeOptions.forEachIndexed { index, time ->
                    OMTeamCard(
                        text = time,
                        isSelected = (selectedTime == time),
                        textStyle = PaperlogyType.onboardingCardText,
                        onClick = {
                            // 같은 카드 짝수 회 클릭 시 선택 해제
                            selectedTime = if (selectedTime == time) {
                                ""
                            } else {
                                time
                            }
                            onTimeChange(selectedTime)
                        }
                    )

                    if (index < timeOptions.size - 1) {
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
                    onClick = {
                        if (selectedTime.isNotEmpty()) {
                            onNext()
                        }
                    },
                    height = dp60,
                    cornerRadius = dp8,
                    backgroundColor = if (selectedTime.isNotEmpty()) Green07 else Green04,
                    modifier = Modifier.width(dp200)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SetTimeScreenPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            SetTimeScreen()
        }
    }
}