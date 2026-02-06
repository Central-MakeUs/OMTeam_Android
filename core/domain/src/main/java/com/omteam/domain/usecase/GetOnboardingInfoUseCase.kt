package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class GetOnboardingInfoUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(): Flow<Result<OnboardingInfo>> = 
        onboardingRepository.getOnboardingInfo()
}