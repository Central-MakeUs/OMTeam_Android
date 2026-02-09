package com.omteam.data.mapper

import com.omteam.domain.model.chat.ChatHistory
import com.omteam.domain.model.chat.ChatMessage
import com.omteam.domain.model.chat.ChatOption
import com.omteam.domain.model.chat.ChatRole
import com.omteam.network.dto.chat.ChatHistoryData
import com.omteam.network.dto.chat.ChatMessageData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ChatMessageData -> ChatMessage 도메인 모델
 */
fun ChatMessageData.toDomain(): ChatMessage = ChatMessage(
    messageId = messageId,
    role = ChatRole.fromString(role),
    content = content,
    options = options?.map { it.toDomain() },
    actionType = actionType,
    createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME),
    terminal = terminal
)

/**
 * ChatOption DTO -> ChatOption 도메인 모델
 */
fun com.omteam.network.dto.chat.ChatOption.toDomain(): ChatOption = ChatOption(
    label = label,
    value = value,
    actionType = actionType
)

/**
 * ChatHistoryData -> ChatHistory 도메인 모델
 */
fun ChatHistoryData.toDomain(): ChatHistory = ChatHistory(
    hasActiveSession = hasActiveSession,
    hasMore = hasMore,
    nextCursor = nextCursor,
    messages = messages.map { it.toDomain() }
)