package com.omteam.domain.repository

import com.omteam.domain.model.chat.ChatHistory
import com.omteam.domain.model.chat.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /**
     * 챗봇에게 메시지 전송
     *
     * @param type 메시지 타입 (TEXT, OPTION)
     * @param value 사용자가 입력한 텍스트 또는 선택된 옵션의 값
     * @param optionValue 옵션 선택 시의 추가 값 (예: SUCCESS, FAILURE)
     * @param actionType 액션 타입 (예: COMPLETE_MISSION, MISSION_FAILURE_REASON)
     * @return 챗봇 응답 메시지 Flow
     */
    fun sendMessage(
        type: String? = null,
        value: String? = null,
        optionValue: String? = null,
        actionType: String? = null
    ): Flow<Result<ChatMessage>>
    
    /**
     * 대화 내역 조회
     * 
     * @param cursor 마지막으로 조회한 messageId (없으면 최신부터 조회)
     * @param size 조회할 메시지 수
     * @return 대화 내역 Flow
     */
    fun getChatHistory(
        cursor: Int? = null,
        size: Int? = null
    ): Flow<Result<ChatHistory>>
}