package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.AuthRepository

/**
 * 운동 가능 시간 수정 UseCase
 */
class UpdateAvailableTimeUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(availableStartTime: String, availableEndTime: String): Result<OnboardingInfo> = 
        authRepository.updateAvailableTime(availableStartTime, availableEndTime)
}