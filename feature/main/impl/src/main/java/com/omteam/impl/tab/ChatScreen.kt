package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.viewmodel.ChatViewModel
import com.omteam.omt.core.designsystem.R

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val sendMessageUiState by viewModel.sendMessageUiState.collectAsState()
    val chatHistoryUiState by viewModel.chatHistoryUiState.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(dp20)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.screen_inner_logo),
                contentDescription = "왼쪽 상단 로고",
                modifier = Modifier
                    .size(dp50)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(dp134))

            SimpleChatBubbleWithX(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(dp48))

            OMTeamText(
                text = stringResource(com.omteam.main.impl.R.string.chat_screen_middle_text),
                style = PretendardType.button02Disabled,
                color = Gray10,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(dp20))
        }

        OMTeamButton(
            text = stringResource(com.omteam.main.impl.R.string.chat_screen_button),
            onClick = {
                // 빈 요청으로 채팅 시작
                viewModel.startChat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dp20)
        )
    }
}

@Composable
fun SimpleChatBubbleWithX(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(180.dp, 170.dp)) {
        Image(
            painter = painterResource(id = R.drawable.character_embarrassed_yellow),
            contentDescription = "대화 없음",
            modifier = Modifier
                .size(dp160)
                .offset(x = dp0, y = dp10)
        )

        // 녹색 말풍선 이미지
        Image(
            painter = painterResource(id = R.drawable.x_green_background),
            contentDescription = "녹색 말풍선",
            modifier = Modifier
                .size(dp48, dp54)
                .offset(x = dp130, y = dp0)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatScreen()
}