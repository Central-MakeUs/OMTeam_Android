package com.omteam.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.component.textfield.OMTeamTextField
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.PaperlogyType

@Composable
fun NicknameOnboardingScreen(
    initialNickname: String = "",
    onNicknameChange: (String) -> Unit = {},
    onNext: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 닉네임 조건 : 8자 이내 한글, 영어, 숫자 사용 가능
        var nickname by remember { mutableStateOf(initialNickname) }

        OMTeamText(
            text = "OMT에서 사용하실\n닉네임을 알려주세요!",
            style = PaperlogyType.headline02
        )

        Spacer(modifier = Modifier.height(dp20))

        OMTeamTextField(
            placeholder = "닉네임을 입력해주세요. (최대 8글자)",
            value = nickname,
            onValueChange = { 
                nickname = it
                onNicknameChange(it)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        OMTeamButton(
            text = "다음",
            onClick = {
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dp20)
        )
    }
}

@Preview
@Composable
private fun NicknameOnboardingScreenPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            NicknameOnboardingScreen()
        }
    }
}