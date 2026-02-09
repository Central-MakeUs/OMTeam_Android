package com.omteam.network.dto.chat

import kotlinx.serialization.Serializable

/**
 * 챗봇 메시지 전송 요청
 *
 * 빈 요청은 채팅 시작(AI 인사)으로 처리되며, 세션이 없으면 자동 생성
 *
 * @property type 메시지 타입 (TEXT, OPTION)
 * @property value 사용자가 입력한 텍스트 또는 선택된 옵션의 값
 * @property optionValue 옵션 선택 시의 추가 값 (예: SUCCESS, FAILURE)
 * @property actionType 액션 타입 (예: COMPLETE_MISSION, MISSION_FAILURE_REASON)
 */
@Serializable
data class ChatMessageRequest(
    val type: String? = null,
    val value: String? = null,
    val optionValue: String? = null,
    val actionType: String? = null
)