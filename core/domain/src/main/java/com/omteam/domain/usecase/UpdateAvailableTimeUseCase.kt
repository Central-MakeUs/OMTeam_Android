package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

/**
 * 운동 가능 시간 수정 UseCase
 */
class UpdateAvailableTimeUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(availableStartTime: String, availableEndTime: String): Flow<Result<OnboardingInfo>> = 
        onboardingRepository.updateAvailableTime(availableStartTime, availableEndTime)
}