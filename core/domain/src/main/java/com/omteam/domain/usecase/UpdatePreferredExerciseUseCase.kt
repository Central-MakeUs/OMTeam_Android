package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.AuthRepository

/**
 * 선호 운동 수정 UseCase
 */
class UpdatePreferredExerciseUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(preferredExercises: List<String>): Result<OnboardingInfo> = 
        authRepository.updatePreferredExercise(preferredExercises)
}