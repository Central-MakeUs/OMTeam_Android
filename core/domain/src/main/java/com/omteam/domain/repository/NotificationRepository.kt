package com.omteam.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * 푸시 알림 관련 Repository
 */
interface NotificationRepository {
    /**
     * FCM 토큰 등록/갱신
     *
     * @param fcmToken FCM 토큰
     * @return 등록 결과 (성공 시 메시지)
     */
    fun registerFcmToken(fcmToken: String): Flow<Result<String>>

    /**
     * FCM 토큰 삭제
     *
     * @return 삭제 결과 (성공 시 메시지)
     */
    fun deleteFcmToken(): Flow<Result<String>>
}