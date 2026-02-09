package com.omteam.network.api

import com.omteam.network.dto.onboarding.OnboardingRequest
import com.omteam.network.dto.onboarding.OnboardingResponse
import com.omteam.network.dto.onboarding.UpdateAvailableTimeRequest
import com.omteam.network.dto.onboarding.UpdateLifestyleRequest
import com.omteam.network.dto.onboarding.UpdateMinExerciseMinutesRequest
import com.omteam.network.dto.onboarding.UpdateAppGoalRequest
import com.omteam.network.dto.onboarding.UpdateNicknameRequest
import com.omteam.network.dto.onboarding.UpdatePreferredExerciseRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface OnboardingApiService {
    
    /**
     * 온보딩 정보 조회
     * 
     * @return 온보딩 정보 (완료하지 않은 경우 에러 리턴)
     */
    @GET("api/onboarding")
    suspend fun getOnboardingInfo(): OnboardingResponse
    
    /**
     * 온보딩 정보 제출
     * 
     * @param request 온보딩 정보
     * @return 온보딩 정보 제출 결과
     */
    @POST("api/onboarding")
    suspend fun submitOnboarding(
        @Body request: OnboardingRequest
    ): OnboardingResponse
    
    /**
     * 평소 생활 패턴 수정
     * 
     * @param request lifestyleType
     * @return 수정된 온보딩 정보
     */
    @PATCH("api/onboarding/lifestyle")
    suspend fun updateLifestyle(
        @Body request: UpdateLifestyleRequest
    ): OnboardingResponse
    
    /**
     * 선호 운동 수정
     * 
     * @param request preferredExercises
     * @return 수정된 온보딩 정보
     */
    @PATCH("api/onboarding/preferred-exercise")
    suspend fun updatePreferredExercise(
        @Body request: UpdatePreferredExerciseRequest
    ): OnboardingResponse
    
    /**
     * 미션에 투자할 수 있는 시간 수정
     * 
     * @param request minExerciseMinutes
     * @return 수정된 온보딩 정보
     */
    @PATCH("api/onboarding/min-exercise-minutes")
    suspend fun updateMinExerciseMinutes(
        @Body request: UpdateMinExerciseMinutesRequest
    ): OnboardingResponse
    
    /**
     * 운동 가능 시간 수정
     *
     * @param request availableStartTime, availableEndTime
     * @return 수정된 온보딩 정보
     */
    @PATCH("api/onboarding/available-time")
    suspend fun updateAvailableTime(
        @Body request: UpdateAvailableTimeRequest
    ): OnboardingResponse

    /**
     * 닉네임 변경
     *
     * @param request nickname
     * @return 수정된 온보딩 정보
     */
    @PATCH("api/onboarding/nickname")
    suspend fun updateNickname(
        @Body request: UpdateNicknameRequest
    ): OnboardingResponse

    /**
     * 앱 사용 목적 수정
     *
     * @param request appGoalText
     * @return 수정된 온보딩 정보
     */
    @PATCH("api/onboarding/app-goal")
    suspend fun updateAppGoal(
        @Body request: UpdateAppGoalRequest
    ): OnboardingResponse
}
