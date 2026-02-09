package com.omteam.network.api

import com.omteam.network.dto.chat.ChatHistoryResponse
import com.omteam.network.dto.chat.ChatMessageRequest
import com.omteam.network.dto.chat.ChatMessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {
    
    /**
     * 챗봇에게 메시지 전송하고 응답 받기
     * 
     * 빈 요청은 채팅 시작(AI 인사)으로 처리되며, 세션이 없으면 자동 생성됩니다.
     * 
     * @param request 메시지 요청 (빈 요청 가능)
     * @return 챗봇 응답 메시지
     */
    @POST("api/chat/messages")
    suspend fun sendMessage(@Body request: ChatMessageRequest = ChatMessageRequest()): ChatMessageResponse
    
    /**
     * 대화 내역 조회
     * 
     * @param cursor 마지막으로 조회한 messageId (없으면 최신부터 조회)
     * @param size 조회할 메시지 수 (기본값: 20)
     * @return 대화 내역
     */
    @GET("api/chat")
    suspend fun getChatHistory(
        @Query("cursor") cursor: Int? = null,
        @Query("size") size: Int? = null
    ): ChatHistoryResponse
}