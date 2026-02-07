package com.omteam.domain.model.chat

import java.time.LocalDateTime

/**
 * 챗봇 메시지
 * 
 * @property messageId 메시지 ID
 * @property role 메시지 역할
 * @property content 메시지 내용
 * @property options 선택 가능한 옵션 목록
 * @property createdAt 생성 시간
 * @property terminal 대화 종료 여부
 */
data class ChatMessage(
    val messageId: Int,
    val role: ChatRole,
    val content: String,
    val options: List<ChatOption>? = null,
    val createdAt: LocalDateTime,
    val terminal: Boolean
)

/**
 * 챗봇 선택 옵션
 * 
 * @property label 화면에 표시할 레이블
 * @property value 서버에 전송할 값
 */
data class ChatOption(
    val label: String,
    val value: String
)

/**
 * 메시지 역할
 */
enum class ChatRole {
    USER,
    ASSISTANT,
    UNKNOWN;
    
    companion object {
        fun fromString(value: String): ChatRole =
            ChatRole.entries.find { it.name == value.uppercase() } ?: UNKNOWN
    }
}

/**
 * 대화 내역
 * 
 * @property hasActiveSession 활성 세션 존재 여부
 * @property hasMore 추가 메시지 존재 여부
 * @property nextCursor 다음 페이지 커서
 * @property messages 메시지 목록
 */
data class ChatHistory(
    val hasActiveSession: Boolean,
    val hasMore: Boolean,
    val nextCursor: Int? = null,
    val messages: List<ChatMessage>
)