package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.chat.ChatHistory
import com.omteam.domain.model.chat.ChatMessage

/**
 * 메시지 전송 UI 상태
 */
sealed interface SendMessageUiState {
    /** 초기 상태 */
    data object Idle : SendMessageUiState
    
    /** 메시지 전송 중 */
    data object Loading : SendMessageUiState
    
    /** 메시지 전송 성공 */
    data class Success(val message: ChatMessage) : SendMessageUiState
    
    /** 메시지 전송 실패 */
    data class Error(val message: String) : SendMessageUiState
}

/**
 * 대화 내역 조회 UI 상태
 */
sealed interface ChatHistoryUiState {
    /** 초기 상태 */
    data object Idle : ChatHistoryUiState
    
    /** 대화 내역 조회 중 */
    data object Loading : ChatHistoryUiState
    
    /** 대화 내역 조회 성공 */
    data class Success(val chatHistory: ChatHistory) : ChatHistoryUiState
    
    /** 대화 내역 조회 실패 */
    data class Error(val message: String) : ChatHistoryUiState
}