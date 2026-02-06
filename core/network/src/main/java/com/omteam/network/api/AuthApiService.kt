package com.omteam.network.api

import com.omteam.network.dto.auth.LoginWithIdTokenRequest
import com.omteam.network.dto.auth.LoginWithIdTokenResponse
import com.omteam.network.dto.onboarding.OnboardingRequest
import com.omteam.network.dto.onboarding.OnboardingResponse
import com.omteam.network.dto.onboarding.UpdateAvailableTimeRequest
import com.omteam.network.dto.onboarding.UpdateLifestyleRequest
import com.omteam.network.dto.onboarding.UpdateMinExerciseMinutesRequest
import com.omteam.network.dto.onboarding.UpdatePreferredExerciseRequest
import com.omteam.network.dto.auth.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    
    /**
     * 소셜 로그인 idToken으로 서버 인증
     *
     * @param provider google, kakao
     * @param request 로그인 요청
     * @return 서버 인증 응답 (accessToken, refreshToken 등)
     */
    @POST("auth/oauth/{provider}")
    suspend fun loginWithIdToken(
        @Path("provider") provider: String,
        @Body request: LoginWithIdTokenRequest
    ): LoginWithIdTokenResponse
    
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
     * 토큰 갱신
     * 
     * @param request refreshToken
     * @return 새 accessToken, refreshToken
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): LoginWithIdTokenResponse
    
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
}