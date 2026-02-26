package com.omteam.data.repository

import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.repository.NotificationRepository
import com.omteam.network.api.NotificationApiService
import com.omteam.network.dto.notification.FcmTokenRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * NotificationRepository 구현체
 */
class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiService: NotificationApiService
) : NotificationRepository {

    override fun registerFcmToken(fcmToken: String): Flow<Result<String>> =
        safeApiCall(
            logTag = "FCM 토큰 등록",
            apiCall = {
                val request = FcmTokenRequest(fcmToken = fcmToken)
                notificationApiService.registerFcmToken(request)
            },
            transform = { response -> response.data },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun deleteFcmToken(): Flow<Result<String>> =
        safeApiCall(
            logTag = "FCM 토큰 삭제",
            apiCall = { notificationApiService.deleteFcmToken() },
            transform = { response -> response.data },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
}