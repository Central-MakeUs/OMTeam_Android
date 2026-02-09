package com.omteam.domain.usecase

import com.omteam.domain.model.chat.ChatMessage
import com.omteam.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

/**
 * 챗봇에게 메시지 전송 UseCase
 *
 * 빈 요청은 채팅 시작(AI 인사)으로 처리되며, 세션이 없으면 자동 생성
 */
class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    /**
     * 챗봇에게 메시지 전송
     *
     * @param type 메시지 타입 (TEXT, OPTION)
     * @param value 사용자가 입력한 텍스트 또는 선택된 옵션의 값
     * @param optionValue 옵션 선택 시의 추가 값 (예: SUCCESS, FAILURE)
     * @param actionType 액션 타입 (예: COMPLETE_MISSION)
     * @return 챗봇 응답 메시지
     */
    operator fun invoke(
        type: String? = null,
        value: String? = null,
        optionValue: String? = null,
        actionType: String? = null
    ): Flow<Result<ChatMessage>> =
        chatRepository.sendMessage(type = type, value = value, optionValue = optionValue, actionType = actionType)
}