package com.omteam.impl.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.ImeAction
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
import com.omteam.designsystem.component.textfield.OMTeamBorderlessTextField
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.domain.model.chat.ChatHistory
import com.omteam.domain.model.chat.ChatMessage
import com.omteam.domain.model.chat.ChatOption
import com.omteam.domain.model.chat.ChatRole
import com.omteam.impl.viewmodel.ChatViewModel
import com.omteam.impl.viewmodel.state.ChatHistoryUiState
import com.omteam.impl.viewmodel.state.SendMessageUiState
import com.omteam.omt.core.designsystem.R
import java.time.LocalDateTime

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

    // 마지막 메시지 목록을 캐싱해서 로딩 중에도 표시
    var cachedMessages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var previousMessageCount by remember { mutableStateOf(0) }
    // 화면 재진입 시 스크롤 필요 여부
    var shouldScrollToBottom by remember { mutableStateOf(true) }

    // 메시지가 실제로 있는지 체크해서 hasActiveSession이 false, messages가 비었으면 초기 화면 표시
    val currentMessages = when (val state = chatHistoryUiState) {
        is ChatHistoryUiState.Success -> {
            cachedMessages = state.chatHistory.messages
            state.chatHistory.messages
        }

        else -> cachedMessages
    }

    val currentMessageCount = currentMessages.size

    val isLoading = sendMessageUiState is SendMessageUiState.Loading ||
            chatHistoryUiState is ChatHistoryUiState.Loading

    // 화면 진입 시 기존 대화 내역 조회 및 스크롤 플래그 설정
    LaunchedEffect(Unit) {
        viewModel.fetchChatHistory()
        shouldScrollToBottom = true
    }

    // 메시지 개수 변경, 로딩, 화면 재진입 시 스크롤
    LaunchedEffect(currentMessageCount, isLoading, shouldScrollToBottom) {
        if (shouldScrollToBottom || (currentMessageCount > previousMessageCount) || isLoading) {
            if (scrollState.maxValue > 0) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
            previousMessageCount = currentMessageCount
            shouldScrollToBottom = false
        }
    }

    ChatScreenContent(
        modifier = modifier,
        currentMessages = currentMessages,
        isLoading = isLoading,
        chatHistoryUiState = chatHistoryUiState,
        sendMessageUiState = sendMessageUiState,
        scrollState = scrollState,
        onOptionSelected = { messageId, option ->
            // 이미 선택된 경우 무시해서 API 중복 호출 방지
            if (!selectedMessageIds.contains(messageId)) {
                selectedMessageIds = selectedMessageIds + messageId
                viewModel.sendMessage(
                    type = "OPTION",
                    value = option.label,
                    optionValue = option.value,
                    actionType = option.actionType
                )
            }
        },
        onStartChat = {
            // 선택 상태 초기화하고 새 채팅 시작
            selectedMessageIds = emptySet()
            previousMessageCount = 0
            viewModel.startChat()
        },
        onFetchChatHistory = {
            viewModel.fetchChatHistory()
        },
        isOptionDisabled = { messageId ->
            selectedMessageIds.contains(messageId)
        },
        onSendMessage = { message ->
            // 실패 사유 전송
            viewModel.sendMessage(
                type = "TEXT",
                value = message,
                optionValue = null,
                actionType = "MISSION_FAILURE_REASON"
            )
        }
    )
}

@Composable
fun ChatScreenContent(
    modifier: Modifier = Modifier,
    currentMessages: List<ChatMessage>,
    isLoading: Boolean,
    chatHistoryUiState: ChatHistoryUiState,
    sendMessageUiState: SendMessageUiState,
    scrollState: ScrollState,
    onOptionSelected: (Int, ChatOption) -> Unit,
    onStartChat: () -> Unit,
    onFetchChatHistory: () -> Unit,
    isOptionDisabled: (Int) -> Boolean,
    onSendMessage: (String) -> Unit = {}
) {
    val hasMessages = currentMessages.isNotEmpty()
    var messageText by remember { mutableStateOf("") }

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
            } else if (chatHistoryUiState is ChatHistoryUiState.Error && currentMessages.isEmpty()) {
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
                    onClick = onFetchChatHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dp20)
                )
            } else {
                // 채팅 메시지 표시
                Spacer(modifier = Modifier.height(dp20))

                // 로딩 중 이전 메시지 유지
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
                                    onOptionSelected = { option ->
                                        onOptionSelected(message.messageId, option)
                                    },
                                    isOptionDisabled = isOptionDisabled(message.messageId)
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

        // 채팅 시작하기 버튼 또는 메시지 입력 영역
        if (!hasMessages && chatHistoryUiState !is ChatHistoryUiState.Error) {
            // 초기 상태 - 채팅 시작하기 버튼
            OMTeamButton(
                text = stringResource(com.omteam.main.impl.R.string.chat_screen_button),
                onClick = onStartChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dp20)
            )
        } else if (hasMessages || chatHistoryUiState is ChatHistoryUiState.Success) {
            // 채팅 중 - 메시지 입력 영역
            ChatInputArea(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText)
                        messageText = ""
                    }
                },
                isEnabled = !isLoading
            )
        }
    }
}

/**
 * 메시지 입력 영역
 *
 * 하단 탭 바로 위에 표시되는 메시지 입력 필드와 전송 버튼
 */
@Composable
private fun ChatInputArea(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Gray01)
            .border(
                width = dp1,
                color = GreenSub02,
                shape = RoundedCornerShape(dp0)
            )
            .padding(horizontal = dp20, vertical = dp8),
        horizontalArrangement = Arrangement.spacedBy(dp8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 메시지 입력란
        Box(
            modifier = Modifier
                .weight(1f)
                .height(dp48)
                .clip(RoundedCornerShape(dp8))
                .background(White)
                .border(
                    width = dp1,
                    color = GreenSub03Button,
                    shape = RoundedCornerShape(dp8)
                )
                .padding(horizontal = dp12, vertical = dp15),
            contentAlignment = Alignment.CenterStart
        ) {
            OMTeamBorderlessTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                enabled = isEnabled,
                placeholder = "메시지를 입력해주세요.",
                textStyle = PretendardType.body03_1,
                placeholderColor = Gray07,
                textColor = Gray12,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (messageText.isNotBlank()) {
                            onSendClick()
                        }
                    }
                ),
                singleLine = true
            )
        }

        // 전송 버튼
        Box(
            modifier = Modifier
                .size(dp48)
                .clip(RoundedCornerShape(dp10))
                .background(Green07)
                .clickable(
                    enabled = isEnabled && messageText.isNotBlank(),
                    onClick = onSendClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_arrow_up),
                contentDescription = "전송",
                modifier = Modifier.size(dp24)
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
    onOptionSelected: (ChatOption) -> Unit,
    modifier: Modifier = Modifier,
    isOptionDisabled: Boolean = false
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
                            onClick = {
                                if (!isOptionDisabled) {
                                    onOptionSelected(option)
                                }
                            },
                            isEnabled = !isOptionDisabled
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
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
private fun ChatScreenContentPreview() {
    val sampleMessages = listOf(
        ChatMessage(
            messageId = 1,
            role = ChatRole.ASSISTANT,
            content = "안녕하세요! **OMT**입니다. 오늘은 어떤 미션을 수행하고 싶으신가요?",
            options = listOf(
                ChatOption(label = "운동 미션", value = "exercise", actionType = "START_MISSION"),
                ChatOption(label = "식단 미션", value = "diet", actionType = "START_MISSION"),
                ChatOption(label = "수면 미션", value = "sleep", actionType = "START_MISSION")
            ),
            createdAt = LocalDateTime.now(),
            terminal = false
        ),
        ChatMessage(
            messageId = 2,
            role = ChatRole.USER,
            content = "운동 미션",
            options = emptyList(),
            createdAt = LocalDateTime.now(),
            terminal = false
        ),
        ChatMessage(
            messageId = 3,
            role = ChatRole.ASSISTANT,
            content = "좋은 선택이에요! **30분 걷기** 미션을 시작해보세요.",
            options = listOf(
                ChatOption(label = "미션 시작하기", value = "start", actionType = "CONFIRM")
            ),
            createdAt = LocalDateTime.now(),
            terminal = false
        )
    )

    ChatScreenContent(
        currentMessages = sampleMessages,
        isLoading = false,
        chatHistoryUiState = ChatHistoryUiState.Success(
            ChatHistory(
                hasActiveSession = true,
                hasMore = false,
                messages = sampleMessages
            )
        ),
        sendMessageUiState = SendMessageUiState.Idle,
        scrollState = rememberScrollState(),
        onOptionSelected = { _, _ -> },
        onStartChat = {},
        onFetchChatHistory = {},
        isOptionDisabled = { false },
        onSendMessage = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenContentEmptyPreview() {
    ChatScreenContent(
        currentMessages = emptyList(),
        isLoading = false,
        chatHistoryUiState = ChatHistoryUiState.Idle,
        sendMessageUiState = SendMessageUiState.Idle,
        scrollState = rememberScrollState(),
        onOptionSelected = { _, _ -> },
        onStartChat = {},
        onFetchChatHistory = {},
        isOptionDisabled = { false },
        onSendMessage = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AssistantMessageBubblePreview() {
    val sampleMessage = ChatMessage(
        messageId = 1,
        role = ChatRole.ASSISTANT,
        content = "안녕하세요! **OMT**입니다.\n오늘은 어떤 미션을 수행하고 싶으신가요?",
        options = listOf(
            ChatOption(label = "운동 미션", value = "exercise", actionType = "START_MISSION"),
            ChatOption(label = "식단 미션", value = "diet", actionType = "START_MISSION")
        ),
        createdAt = LocalDateTime.now(),
        terminal = false
    )

    AssistantMessageBubble(
        message = sampleMessage,
        onOptionSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun UserMessageBubblePreview() {
    UserMessageBubble(text = "운동 미션 시작해줘")
}