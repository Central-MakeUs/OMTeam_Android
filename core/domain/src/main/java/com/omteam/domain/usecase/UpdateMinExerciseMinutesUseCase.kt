package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.AuthRepository

/**
 * 미션에 투자할 수 있는 시간 수정 UseCase
 */
class UpdateMinExerciseMinutesUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(minExerciseMinutes: Int): Result<OnboardingInfo> = 
        authRepository.updateMinExerciseMinutes(minExerciseMinutes)
}