package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class UpdateNicknameUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(nickname: String): Flow<Result<OnboardingInfo>> =
        onboardingRepository.updateNickname(nickname)
}