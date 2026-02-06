package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

/**
 * 평소 생활 패턴 수정 UseCase
 */
class UpdateLifestyleUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(lifestyleType: String): Flow<Result<OnboardingInfo>> = 
        onboardingRepository.updateLifestyle(lifestyleType)
}