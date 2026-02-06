package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

/**
 * 선호 운동 수정 UseCase
 */
class UpdatePreferredExerciseUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(preferredExercises: List<String>): Flow<Result<OnboardingInfo>> = 
        onboardingRepository.updatePreferredExercise(preferredExercises)
}