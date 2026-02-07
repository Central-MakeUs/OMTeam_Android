package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omteam.designsystem.component.button.OMTeamButton
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.chat.ChatMessage
import com.omteam.domain.model.chat.ChatOption
import com.omteam.domain.model.chat.ChatRole
import com.omteam.impl.viewmodel.ChatViewModel
import com.omteam.impl.viewmodel.state.ChatHistoryUiState
import com.omteam.impl.viewmodel.state.SendMessageUiState
import com.omteam.omt.core.designsystem.R
import timber.log.Timber

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val sendMessageUiState by viewModel.sendMessageUiState.collectAsState()
    val chatHistoryUiState by viewModel.chatHistoryUiState.collectAsState()
    
    // 선택한 옵션을 추적 (messageId -> option)
    var selectedOptions by remember { mutableStateOf<Map<Int, ChatOption>>(emptyMap()) }

    val hasMessages = chatHistoryUiState is ChatHistoryUiState.Success
    
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
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.screen_inner_logo),
                contentDescription = "왼쪽 상단 로고",
                modifier = Modifier
                    .size(dp50)
                    .align(Alignment.Start)
            )

            if (!hasMessages) {
                // 채팅 내역이 없을 때 초기 화면
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
            } else {
                // 채팅 메시지 표시
                Spacer(modifier = Modifier.height(dp20))
                
                when (val state = chatHistoryUiState) {
                    is ChatHistoryUiState.Success -> {
                        state.chatHistory.messages.forEachIndexed { index, message ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(dp20))
                            }
                            
                            when (message.role) {
                                ChatRole.ASSISTANT -> {
                                    AssistantMessageBubble(
                                        message = message,
                                        isOptionDisabled = selectedOptions.containsKey(message.messageId),
                                        onOptionSelected = { option ->
                                            // 이미 선택된 경우 무시
                                            if (!selectedOptions.containsKey(message.messageId)) {
                                                selectedOptions = selectedOptions + (message.messageId to option)
                                                viewModel.sendMessage(
                                                    type = "TEXT",
                                                    text = option.label,
                                                    value = option.value
                                                )
                                            }
                                        }
                                    )
                                    
                                    // 선택한 옵션 표시
                                    selectedOptions[message.messageId]?.let { option ->
                                        Spacer(modifier = Modifier.height(dp20))
                                        UserMessageBubble(text = option.label)
                                    }
                                }
                                ChatRole.USER -> {
                                    UserMessageBubble(text = message.content)
                                }
                                ChatRole.UNKNOWN -> {}
                            }
                        }
                        
                        // 로딩 중일 때 "..." 표시
                        if (sendMessageUiState is SendMessageUiState.Loading) {
                            Spacer(modifier = Modifier.height(dp20))
                            AssistantMessageBubble(
                                message = null,
                                onOptionSelected = {}
                            )
                        }
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(dp20))
        }

        // 채팅 시작하기 버튼
        if (!hasMessages) {
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
}

/**
 * AI 메시지 말풍선
 */
@Composable
fun AssistantMessageBubble(
    message: ChatMessage?,
    isOptionDisabled: Boolean = false,
    onOptionSelected: (ChatOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // 메시지 내용 (최대 가로 길이 253dp)
        Box(
            modifier = Modifier
                .widthIn(max = 253.dp)
                .clip(RoundedCornerShape(topStart = dp0, topEnd = dp12, bottomEnd = dp12, bottomStart = dp12))
                .background(Gray02)
                .padding(horizontal = dp12, vertical = dp11)
        ) {
            val content = message?.content ?: "..."
            val annotatedText = buildAnnotatedStringWithBold(content)
            
            Text(
                text = annotatedText,
                style = PretendardType.body02_2.copy(
                    lineHeight = 25.6.sp,
                    color = Gray12
                )
            )
        }
        
        // 옵션 버튼들
        message?.options?.let { options ->
            if (options.isNotEmpty()) {
                Spacer(modifier = Modifier.height(dp12))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(dp8)
                ) {
                    options.forEach { option ->
                        OptionButton(
                            option = option,
                            isEnabled = !isOptionDisabled,
                            onClick = { 
                                if (!isOptionDisabled) {
                                    onOptionSelected(option)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * **텍스트**를 볼드체로 변환하는 함수
 */
fun buildAnnotatedStringWithBold(text: String) = buildAnnotatedString {
    val boldPattern = """\*\*(.+?)\*\*""".toRegex()
    var lastIndex = 0

    boldPattern.findAll(text).forEach { matchResult ->
        // ** 이전의 일반 텍스트
        append(text.substring(lastIndex, matchResult.range.first))

        // ** ** 안의 볼드 텍스트
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(matchResult.groupValues[1])
        }

        lastIndex = matchResult.range.last + 1
    }

    // 남은 일반 텍스트
    if (lastIndex < text.length) {
        append(text.substring(lastIndex))
    }
}

/**
 * 옵션 선택 버튼
 */
@Composable
fun OptionButton(
    option: ChatOption,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .border(
                width = 1.dp,
                color = Green03,
                shape = RoundedCornerShape(32.dp)
            )
            .background(Green02)
            .then(
                if (isEnabled) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = dp20, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        OMTeamText(
            text = option.label,
            style = PaperlogyType.button02,
            color = Gray12,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 사용자 메시지 말풍선 (우측 정렬)
 */
@Composable
fun UserMessageBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomEnd = 0.dp, bottomStart = 12.dp))
                .background(Green05)
                .padding(horizontal = 12.dp, vertical = 11.dp)
        ) {
            OMTeamText(
                text = text,
                style = PretendardType.body02_2.copy(
                    lineHeight = 25.6.sp
                ),
                color = Gray12,
                textAlign = TextAlign.Right
            )
        }
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
