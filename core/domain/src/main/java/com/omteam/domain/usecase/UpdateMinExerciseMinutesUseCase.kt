package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

/**
 * 미션에 투자할 수 있는 시간 수정 UseCase
 */
class UpdateMinExerciseMinutesUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(minExerciseMinutes: Int): Flow<Result<OnboardingInfo>> = 
        onboardingRepository.updateMinExerciseMinutes(minExerciseMinutes)
}