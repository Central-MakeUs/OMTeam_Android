package com.omteam.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.component.textfield.OMTeamTextField
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.R
import com.omteam.impl.viewmodel.NicknameErrorType
import com.omteam.impl.viewmodel.OnboardingViewModel

@Composable
fun SetNicknameScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNext: () -> Unit = {}
) {
    val onboardingData by viewModel.onboardingData.collectAsState()
    val nicknameErrorType by viewModel.nicknameErrorType.collectAsState()

    val errorMessage = when (nicknameErrorType) {
        NicknameErrorType.LENGTH_EXCEED -> stringResource(R.string.nickname_error_length)
        NicknameErrorType.SPECIAL_CHAR -> stringResource(R.string.nickname_error_special_char)
        NicknameErrorType.NONE -> ""
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OMTeamText(
            text = stringResource(R.string.set_nickname_screen_title),
            style = PaperlogyType.headline02
        )

        Spacer(modifier = Modifier.height(dp20))

        OMTeamTextField(
            placeholder = stringResource(R.string.nickname_text_field_placeholder),
            value = onboardingData.nickname,
            onValueChange = { viewModel.updateNickname(it) },
            isError = nicknameErrorType != NicknameErrorType.NONE,
            errorMessage = errorMessage
        )

        Spacer(modifier = Modifier.weight(1f))

        OMTeamButton(
            text = stringResource(R.string.next),
            onClick = {
                if (onboardingData.nickname.isNotEmpty() && nicknameErrorType == NicknameErrorType.NONE) {
                    onNext()
                }
            },
            backgroundColor = if (onboardingData.nickname.isNotEmpty() && nicknameErrorType == NicknameErrorType.NONE) Green07 else Green04,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dp20)
        )
    }
}

@Preview
@Composable
private fun SetNicknameScreenPreview() {
    OMTeamTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            SetNicknameScreen()
        }
    }
}