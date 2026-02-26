package com.omteam.network.api

import com.omteam.network.dto.notification.FcmTokenRequest
import com.omteam.network.dto.notification.FcmTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface NotificationApiService {

    /**
     * FCM 토큰 등록/갱신
     *
     * @param request FCM 토큰
     * @return 등록 결과
     */
    @PUT("api/notification/fcm-token")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRequest
    ): FcmTokenResponse

    /**
     * FCM 토큰 삭제
     *
     * @return 삭제 결과
     */
    @DELETE("api/notification/fcm-token")
    suspend fun deleteFcmToken(): FcmTokenResponse
}
