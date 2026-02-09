package com.omteam.data.repository

import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.model.chat.ChatHistory
import com.omteam.domain.model.chat.ChatMessage
import com.omteam.domain.repository.ChatRepository
import com.omteam.network.api.ChatApiService
import com.omteam.network.dto.chat.ChatMessageRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApiService: ChatApiService
) : ChatRepository {

    override fun sendMessage(
        type: String?,
        text: String?,
        value: String?,
        actionType: String?
    ): Flow<Result<ChatMessage>> =
        safeApiCall(
            logTag = "챗봇 메시지 전송",
            defaultErrorMessage = "메시지를 전송할 수 없습니다",
            apiCall = {
                chatApiService.sendMessage(
                    ChatMessageRequest(type = type, text = text, value = value, actionType = actionType)
                )
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun getChatHistory(cursor: Int?, size: Int?): Flow<Result<ChatHistory>> =
        safeApiCall(
            logTag = "대화 내역 조회",
            defaultErrorMessage = "대화 내역을 불러올 수 없습니다",
            apiCall = { chatApiService.getChatHistory(cursor = cursor, size = size) },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
}