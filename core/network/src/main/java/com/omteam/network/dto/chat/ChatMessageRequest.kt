package com.omteam.network.dto.chat

import kotlinx.serialization.Serializable

/**
 * 챗봇 메시지 전송 요청
 * 
 * 빈 요청은 채팅 시작(AI 인사)으로 처리되며, 세션이 없으면 자동 생성됩니다.
 * 
 * @property type 메시지 타입 (예: TEXT)
 * @property text 사용자가 입력한 텍스트
 * @property value 선택된 옵션의 값 (예: TIME_SHORTAGE, EXERCISE_HARD)
 */
@Serializable
data class ChatMessageRequest(
    val type: String? = null,
    val text: String? = null,
    val value: String? = null
)