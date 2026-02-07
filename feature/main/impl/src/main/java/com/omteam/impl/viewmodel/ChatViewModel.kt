package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.usecase.GetChatHistoryUseCase
import com.omteam.domain.usecase.SendMessageUseCase
import com.omteam.impl.viewmodel.state.ChatHistoryUiState
import com.omteam.impl.viewmodel.state.SendMessageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [com.omteam.impl.tab.ChatScreen] 뷰모델
 * 
 * 챗봇 메시지 전송 및 대화 내역 조회 관리
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase
) : ViewModel() {

    // 메시지 전송 UI State
    private val _sendMessageUiState = MutableStateFlow<SendMessageUiState>(SendMessageUiState.Idle)
    val sendMessageUiState: StateFlow<SendMessageUiState> = _sendMessageUiState.asStateFlow()
    
    // 대화 내역 조회 UI State
    private val _chatHistoryUiState = MutableStateFlow<ChatHistoryUiState>(ChatHistoryUiState.Idle)
    val chatHistoryUiState: StateFlow<ChatHistoryUiState> = _chatHistoryUiState.asStateFlow()

    /**
     * 채팅 시작하기 (빈 요청)
     * 
     * AI 인사 메시지를 받아오며 세션이 없으면 자동 생성
     */
    fun startChat() = viewModelScope.launch {
        _sendMessageUiState.value = SendMessageUiState.Loading
        
        sendMessageUseCase()
            .collect { result ->
                _sendMessageUiState.value = result.fold(
                    onSuccess = { message ->
                        Timber.d("## 채팅 시작 성공 : $message")
                        
                        // 채팅 시작 후 대화 내역 조회
                        fetchChatHistory()
                        
                        SendMessageUiState.Success(message)
                    },
                    onFailure = { error ->
                        Timber.e("## 채팅 시작 실패 : ${error.message}")
                        SendMessageUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }

    /**
     * 메시지 전송
     * 
     * @param type 메시지 타입 (예: TEXT)
     * @param text 사용자가 입력한 텍스트
     * @param value 선택된 옵션의 값 (예: TIME_SHORTAGE, EXERCISE_HARD)
     */
    fun sendMessage(
        type: String? = null,
        text: String? = null,
        value: String? = null
    ) = viewModelScope.launch {
        _sendMessageUiState.value = SendMessageUiState.Loading
        
        sendMessageUseCase(type = type, text = text, value = value)
            .collect { result ->
                _sendMessageUiState.value = result.fold(
                    onSuccess = { message ->
                        Timber.d("## 메시지 전송 성공 : $message")
                        
                        // 메시지 전송 후 대화 내역 재조회
                        fetchChatHistory()
                        
                        SendMessageUiState.Success(message)
                    },
                    onFailure = { error ->
                        Timber.e("## 메시지 전송 실패 : ${error.message}")
                        SendMessageUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }

    /**
     * 대화 내역 조회
     * 
     * @param cursor 마지막으로 조회한 messageId (없으면 최신부터 조회)
     * @param size 조회할 메시지 수
     */
    fun fetchChatHistory(
        cursor: Int? = null,
        size: Int? = null
    ) = viewModelScope.launch {
        _chatHistoryUiState.value = ChatHistoryUiState.Loading
        
        getChatHistoryUseCase(cursor = cursor, size = size)
            .collect { result ->
                _chatHistoryUiState.value = result.fold(
                    onSuccess = { chatHistory ->
                        Timber.d("## 대화 내역 조회 성공 : $chatHistory")
                        ChatHistoryUiState.Success(chatHistory)
                    },
                    onFailure = { error ->
                        Timber.e("## 대화 내역 조회 실패 : ${error.message}")
                        ChatHistoryUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }
}
