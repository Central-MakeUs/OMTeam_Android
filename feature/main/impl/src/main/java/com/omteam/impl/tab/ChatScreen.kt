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

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val sendMessageUiState by viewModel.sendMessageUiState.collectAsState()
    val chatHistoryUiState by viewModel.chatHistoryUiState.collectAsState()

    // 이미 선택한 messageId들을 추적해서 API 중복 호출 방지
    var selectedMessageIds by remember { mutableStateOf<Set<Int>>(emptySet()) }

    val scrollState = rememberScrollState()

    // 마지막 성공적인 메시지 목록을 캐싱 (로딩 중에도 표시하기 위해)
    var cachedMessages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var previousMessageCount by remember { mutableStateOf(0) }

    // 메시지가 실제로 있는지 체크해서 hasActiveSession이 false, messages가 비었으면 초기 화면 표시
    val currentMessages = when (val state = chatHistoryUiState) {
        is ChatHistoryUiState.Success -> {
            cachedMessages = state.chatHistory.messages
            state.chatHistory.messages
        }

        else -> cachedMessages
    }

    val hasMessages = currentMessages.isNotEmpty()
    val currentMessageCount = currentMessages.size

    val isLoading = sendMessageUiState is SendMessageUiState.Loading ||
            chatHistoryUiState is ChatHistoryUiState.Loading

    // 화면 진입 시 기존 대화 내역 조회
    LaunchedEffect(Unit) {
        viewModel.fetchChatHistory()
    }

    // 메시지 개수가 실제로 바뀐 때만 스크롤
    LaunchedEffect(currentMessageCount, isLoading) {
        if ((currentMessageCount > previousMessageCount) || isLoading) {
            previousMessageCount = currentMessageCount
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

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
                .verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(id = R.drawable.screen_inner_logo),
                contentDescription = "왼쪽 상단 로고",
                modifier = Modifier
                    .size(dp50)
                    .align(Alignment.Start)
            )

            if (!hasMessages && !isLoading) {
                // 채팅 내역 없을 때 초기 화면
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
            } else if (isLoading && !hasMessages) {
                // 로딩 중일 때 "..." 표시 (채팅 시작하기 직후)
                Spacer(modifier = Modifier.height(dp20))
                AssistantMessageBubble(
                    message = null,
                    onOptionSelected = {}
                )
            } else if (chatHistoryUiState is ChatHistoryUiState.Error && cachedMessages.isEmpty()) {
                // 캐시된 메시지가 없는 에러 상태 UI
                Spacer(modifier = Modifier.height(dp134))
                OMTeamText(
                    text = "대화 내역을 불러올 수 없습니다.",
                    style = PretendardType.body02_2,
                    color = Gray10,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(dp20))
                OMTeamButton(
                    text = "다시 시도하기",
                    onClick = { viewModel.fetchChatHistory() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dp20)
                )
            } else {
                // 채팅 메시지 표시
                Spacer(modifier = Modifier.height(dp20))

                // 캐싱된 메시지 표시 (로딩 중에도 이전 메시지 유지)
                if (currentMessages.isNotEmpty()) {
                    // 메시지를 시간순(오래된 순) 정렬해서 최신 메시지 맨 밑에 표시
                    val sortedMessages = currentMessages.sortedBy { it.messageId }

                    sortedMessages.forEachIndexed { index, message ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.height(dp20))
                        }

                        when (message.role) {
                            ChatRole.ASSISTANT -> {
                                AssistantMessageBubble(
                                    message = message,
                                    isOptionDisabled = selectedMessageIds.contains(message.messageId),
                                    onOptionSelected = { option ->
                                        // 이미 선택된 경우 무시해서 API 중복 호출 방지
                                        if (!selectedMessageIds.contains(message.messageId)) {
                                            selectedMessageIds =
                                                selectedMessageIds + message.messageId
                                            viewModel.sendMessage(
                                                type = "TEXT",
                                                text = option.label,
                                                value = option.value
                                            )
                                        }
                                    }
                                )
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
            }

            Spacer(modifier = Modifier.height(dp20))
        }

        // 채팅 시작하기 버튼
        if (!hasMessages && chatHistoryUiState !is ChatHistoryUiState.Error) {
            OMTeamButton(
                text = stringResource(com.omteam.main.impl.R.string.chat_screen_button),
                onClick = {
                    // 선택 상태 초기화하고 새 채팅 시작
                    selectedMessageIds = emptySet()
                    previousMessageCount = 0
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
        modifier = modifier
            .widthIn(max = dp253)
            .clip(
                RoundedCornerShape(
                    topStart = dp0,
                    topEnd = dp12,
                    bottomEnd = dp12,
                    bottomStart = dp12
                )
            )
            .background(Gray02)
            .padding(horizontal = dp12, vertical = dp11),
        horizontalAlignment = Alignment.Start
    ) {
        // 메시지 내용
        val content = message?.content ?: "..."
        val annotatedText = buildAnnotatedStringWithBold(content)

        Text(
            text = annotatedText,
            style = PretendardType.body02_2.copy(
                lineHeight = 25.6.sp,
                color = Gray12
            )
        )

        // 회색 박스 내부에 표시되는 옵션 버튼들
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
 * **텍스트**를 볼드체로 변환
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
            .clip(RoundedCornerShape(dp32))
            .border(
                width = dp1,
                color = Green03,
                shape = RoundedCornerShape(dp32)
            )
            .background(Green02)
            .then(
                if (isEnabled) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = dp20, vertical = dp10),
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
                .clip(
                    RoundedCornerShape(
                        topStart = dp12,
                        topEnd = dp12,
                        bottomEnd = dp0,
                        bottomStart = dp12
                    )
                )
                .background(Green05)
                .padding(horizontal = dp12, vertical = dp11)
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
    Box(modifier = modifier.size(dp180, dp170)) {
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
