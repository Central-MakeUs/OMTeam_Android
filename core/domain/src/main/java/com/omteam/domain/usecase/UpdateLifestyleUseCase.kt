package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.AuthRepository

/**
 * 평소 생활 패턴 수정 UseCase
 */
class UpdateLifestyleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(lifestyleType: String): Result<OnboardingInfo> = 
        authRepository.updateLifestyle(lifestyleType)
}