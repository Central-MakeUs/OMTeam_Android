package com.omteam.network.dto.chat

import com.omteam.network.dto.onboarding.ApiError
import kotlinx.serialization.Serializable

/**
 * 대화 내역 조회 응답
 */
@Serializable
data class ChatHistoryResponse(
    val success: Boolean,
    val data: ChatHistoryData? = null,
    val error: ApiError? = null
)

/**
 * 대화 내역 데이터
 * 
 * @property hasActiveSession 활성 세션 존재 여부
 * @property hasMore 추가 메시지 존재 여부
 * @property nextCursor 다음 페이지 커서 (마지막으로 조회한 messageId)
 * @property messages 메시지 목록
 */
@Serializable
data class ChatHistoryData(
    val hasActiveSession: Boolean,
    val hasMore: Boolean,
    val nextCursor: Int? = null,
    val messages: List<ChatMessageData>
)