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
     * @param type 메시지 타입 (예: TEXT, OPTION)
     * @param text 사용자가 입력한 텍스트
     * @param value 선택된 옵션의 값 (예: TIME_SHORTAGE, EXERCISE_HARD, SUCCESS, FAILURE)
     * @param actionType 액션 타입 (예: COMPLETE_MISSION)
     * @return 챗봇 응답 메시지
     */
    operator fun invoke(
        type: String? = null,
        text: String? = null,
        value: String? = null,
        actionType: String? = null
    ): Flow<Result<ChatMessage>> =
        chatRepository.sendMessage(type = type, text = text, value = value, actionType = actionType)
}