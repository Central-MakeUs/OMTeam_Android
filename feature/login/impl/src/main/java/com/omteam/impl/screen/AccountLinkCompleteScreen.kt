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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.PretendardType
import com.omteam.login.impl.R

@Composable
fun AccountLinkCompleteScreen(
    modifier: Modifier = Modifier,
    onNavigateToOnboarding: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = dp20),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(dp200)
                    .background(
                        color = Green03,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = com.omteam.omt.core.designsystem.R.drawable.character_cheer),
                    contentDescription = "계정 연동 완료 화면 캐릭터",
                    modifier = Modifier
                        .width(227.875.dp)
                        .height(197.111.dp)
                        .rotate(6.016f)
                )
            }

            Spacer(modifier = Modifier.height(dp72))

            OMTeamText(
                text = stringResource(R.string.link_complete_title),
                style = PaperlogyType.headline01,
                color = Black
            )

            Spacer(modifier = Modifier.height(dp16))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Green08)) {
                        append(stringResource(R.string.link_complete_subtitle_first))
                    }
                    append(stringResource(R.string.link_complete_subtitle_second))
                },
                style = PretendardType.body02,
                color = Gray09,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            OMTeamButton(
                text = "OMT 시작하기",
                onClick = onNavigateToOnboarding,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dp20)
            )
        }
    }
}

@Preview
@Composable
private fun AccountLinkCompleteScreenPreview() {
    OMTeamTheme {
        AccountLinkCompleteScreen(
            modifier = Modifier.background(Color.White),
            onNavigateToOnboarding = {}
        )
    }
}