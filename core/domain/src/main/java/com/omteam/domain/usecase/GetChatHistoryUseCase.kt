package com.omteam.domain.usecase

import com.omteam.domain.model.chat.ChatHistory
import com.omteam.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

/**
 * 대화 내역 조회 UseCase
 * 
 * 커서 기반 페이지네이션을 통해 대화 내역 조회
 */
class GetChatHistoryUseCase(
    private val chatRepository: ChatRepository
) {
    /**
     * 대화 내역 조회
     * 
     * @param cursor 마지막으로 조회한 messageId (없으면 최신부터 조회)
     * @param size 조회할 메시지 수 (기본값: 20)
     * @return 대화 내역
     */
    operator fun invoke(
        cursor: Int? = null,
        size: Int? = null
    ): Flow<Result<ChatHistory>> =
        chatRepository.getChatHistory(cursor = cursor, size = size)
}