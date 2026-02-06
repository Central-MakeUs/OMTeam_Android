package com.omteam.impl.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omteam.designsystem.component.button.OMTeamSnsButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.Gray05
import com.omteam.designsystem.theme.Gray09
import com.omteam.designsystem.theme.Green02
import com.omteam.designsystem.theme.OMTeamTheme
import com.omteam.designsystem.theme.PretendardType
import com.omteam.designsystem.theme.Yellow14
import com.omteam.omt.core.designsystem.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onKakaoLogin: () -> Unit = {},
    onGoogleLogin: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(Green02)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = dp20),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.character_login),
                contentDescription = "로그인 화면 캐릭터",
                modifier = Modifier.size(dp200)
            )

            Spacer(modifier = Modifier.height(dp48))

            OMTeamText(
                text = stringResource(com.omteam.login.impl.R.string.login_welcome_message),
                style = PretendardType.body01,
                color = Gray09
            )

            Spacer(modifier = Modifier.height(186.dp))
        }

        // SNS 로그인 버튼들
        // 밑에서 52dp 떨어짐
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = dp20)
                .padding(bottom = dp52),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OMTeamSnsButton(
                iconRes = R.drawable.kakao_symbol,
                text = stringResource(com.omteam.login.impl.R.string.login_with_kakao),
                onClick = onKakaoLogin,
                backgroundColor = Yellow14,
            )

            Spacer(modifier = Modifier.height(dp12))

            OMTeamSnsButton(
                iconRes = R.drawable.google_symbol,
                text = stringResource(com.omteam.login.impl.R.string.login_with_google),
                onClick = onGoogleLogin,
                backgroundColor = Color.White,
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    OMTeamTheme {
        LoginScreen(
            modifier = Modifier.background(Color.White),
            onKakaoLogin = {},
            onGoogleLogin = {}
        )
    }
}