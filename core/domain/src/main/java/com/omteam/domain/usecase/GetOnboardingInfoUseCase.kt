package com.omteam.domain.usecase

import com.omteam.domain.model.OnboardingInfo
import com.omteam.domain.repository.AuthRepository

class GetOnboardingInfoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<OnboardingInfo> = 
        authRepository.getOnboardingInfo()
}
