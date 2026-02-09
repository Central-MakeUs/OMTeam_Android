package com.omteam.network.dto.chat

import com.omteam.network.dto.onboarding.ApiError
import kotlinx.serialization.Serializable

/**
 * 챗봇 메시지 전송 응답
 */
@Serializable
data class ChatMessageResponse(
    val success: Boolean,
    val data: ChatMessageData? = null,
    val error: ApiError? = null
)

/**
 * 챗봇 메시지 데이터
 *
 * @property messageId 메시지 ID
 * @property role 메시지 역할 (USER, ASSISTANT)
 * @property content 메시지 내용
 * @property options 선택 가능한 옵션 목록
 * @property actionType 액션 타입 (예: COMPLETE_MISSION)
 * @property createdAt 생성 시간
 * @property terminal 대화 종료 여부
 */
@Serializable
data class ChatMessageData(
    val messageId: Int,
    val role: String,
    val content: String,
    val options: List<ChatOption>? = null,
    val actionType: String? = null,
    val createdAt: String,
    val terminal: Boolean
)

/**
 * 챗봇 선택 옵션
 *
 * @property label 화면에 표시할 레이블
 * @property value 서버에 전송할 값
 * @property actionType 옵션 선택 시 실행할 액션 타입
 */
@Serializable
data class ChatOption(
    val label: String,
    val value: String,
    val actionType: String? = null
)