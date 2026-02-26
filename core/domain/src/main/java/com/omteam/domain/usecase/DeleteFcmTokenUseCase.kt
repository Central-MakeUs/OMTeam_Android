package com.omteam.domain.usecase

import com.omteam.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

/**
 * FCM 토큰 삭제 UseCase
 */
class DeleteFcmTokenUseCase(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<Result<String>> =
        notificationRepository.deleteFcmToken()
}