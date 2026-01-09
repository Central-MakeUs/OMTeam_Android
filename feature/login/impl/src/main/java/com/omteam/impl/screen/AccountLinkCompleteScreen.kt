package com.omteam.impl.screen

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.omteam.api.R
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.dp15
import com.omteam.designsystem.foundation.dp16
import com.omteam.designsystem.foundation.dp20
import com.omteam.designsystem.foundation.dp200
import com.omteam.designsystem.foundation.dp48
import com.omteam.designsystem.foundation.dp72
import com.omteam.designsystem.theme.Black
import com.omteam.designsystem.theme.Gray05
import com.omteam.designsystem.theme.Gray09
import com.omteam.designsystem.theme.Green08
import com.omteam.designsystem.theme.PaperlogyType
import com.omteam.designsystem.theme.PretendardType

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
            Box(
                modifier = Modifier
                    .size(dp200)
                    .background(
                        color = Gray05,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.height(dp72))

            OMTeamText(
                text = stringResource(com.omteam.impl.R.string.link_complete_title),
                style = PaperlogyType.headline01,
                color = Black
            )

            Spacer(modifier = Modifier.height(dp16))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Green08)) {
                        append(stringResource(com.omteam.impl.R.string.link_complete_subtitle_first))
                    }
                    append(stringResource(com.omteam.impl.R.string.link_complete_subtitle_second))
                },
                style = PretendardType.body02,
                color = Gray09,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(dp16))

            Button(onClick = onNavigateToOnboarding) {
                Text("온보딩 화면 이동 (제거예정)")
            }
        }
    }
}