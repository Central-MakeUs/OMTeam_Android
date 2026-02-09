package com.omteam.domain.usecase

import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class UpdateAppGoalUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    /**
     * 앱 사용 목적 수정
     *
     * @param appGoalText 앱 사용 목적
     * @return 수정된 온보딩 정보
     */
    operator fun invoke(appGoalText: String): Flow<Result<OnboardingInfo>> =
        onboardingRepository.updateAppGoal(appGoalText)
}