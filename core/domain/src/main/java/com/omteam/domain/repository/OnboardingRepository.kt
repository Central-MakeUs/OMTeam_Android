package com.omteam.domain.repository

import com.omteam.domain.model.onboarding.OnboardingInfo
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    /**
     * 온보딩 정보 조회
     * 
     * @return 온보딩 정보 (완료하지 않은 경우 실패)
     */
    fun getOnboardingInfo(): Flow<Result<OnboardingInfo>>

    /**
     * 온보딩 정보 제출
     * 
     * @param onboardingInfo 온보딩 정보
     * @return 온보딩 정보 제출 결과
     */
    fun submitOnboarding(onboardingInfo: OnboardingInfo): Flow<Result<OnboardingInfo>>
    
    /**
     * 평소 생활 패턴 수정
     * 
     * @param lifestyleType 생활 패턴
     * @return 수정된 온보딩 정보
     */
    fun updateLifestyle(lifestyleType: String): Flow<Result<OnboardingInfo>>
    
    /**
     * 선호 운동 수정
     * 
     * @param preferredExercises 선호 운동 리스트
     * @return 수정된 온보딩 정보
     */
    fun updatePreferredExercise(preferredExercises: List<String>): Flow<Result<OnboardingInfo>>
    
    /**
     * 미션에 투자할 수 있는 시간 수정
     * 
     * @param minExerciseMinutes 미션에 투자할 수 있는 시간 (분)
     * @return 수정된 온보딩 정보
     */
    fun updateMinExerciseMinutes(minExerciseMinutes: Int): Flow<Result<OnboardingInfo>>
    
    /**
     * 운동 가능 시간 수정
     *
     * @param availableStartTime 시작 시간
     * @param availableEndTime 종료 시간
     * @return 수정된 온보딩 정보
     */
    fun updateAvailableTime(availableStartTime: String, availableEndTime: String): Flow<Result<OnboardingInfo>>

    /**
     * 닉네임 변경
     *
     * @param nickname 닉네임
     * @return 수정된 온보딩 정보
     */
    fun updateNickname(nickname: String): Flow<Result<OnboardingInfo>>
}