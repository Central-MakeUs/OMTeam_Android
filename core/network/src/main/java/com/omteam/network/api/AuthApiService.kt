package com.omteam.network.api

import com.omteam.network.dto.LoginWithIdTokenRequest
import com.omteam.network.dto.LoginWithIdTokenResponse
import com.omteam.network.dto.OnboardingRequest
import com.omteam.network.dto.OnboardingResponse
import com.omteam.network.dto.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.GET
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
}