package com.omteam.domain.usecase

import com.omteam.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

/**
 * FCM 토큰 등록/갱신 UseCase
 */
class RegisterFcmTokenUseCase(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(fcmToken: String): Flow<Result<String>> =
        notificationRepository.registerFcmToken(fcmToken)
}