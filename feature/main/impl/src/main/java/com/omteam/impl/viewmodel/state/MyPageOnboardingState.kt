package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.onboarding.OnboardingInfo

sealed class MyPageOnboardingState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : MyPageOnboardingState()

    /**
     * 로딩 중
     */
    data object Loading : MyPageOnboardingState()

    /**
     * 조회 성공
     */
    data class Success(val data: OnboardingInfo) : MyPageOnboardingState()

    /**
     * 업데이트 성공
     */
    data class UpdateSuccess(val data: OnboardingInfo) : MyPageOnboardingState()

    /**
     * 로딩 실패
     */
    data class Error(val message: String) : MyPageOnboardingState()
}