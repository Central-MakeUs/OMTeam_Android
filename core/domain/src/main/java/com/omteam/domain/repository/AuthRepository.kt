package com.omteam.domain.repository

import com.omteam.domain.model.LoginResult
import com.omteam.domain.model.OnboardingInfo
import com.omteam.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUserInfo(provider: String): Result<UserInfo>
    suspend fun logout(provider: String): Result<Unit>
    
    /**
     * idToken으로 서버 로그인 (구글, 카카오)
     *
     * @param provider 로그인 제공자 ("google", "kakao")
     * @param idToken 소셜 로그인 제공자로부터 받은 ID 토큰
     * @return 서버 로그인 결과 (accessToken, refreshToken 등) Flow
     */
    fun loginWithIdToken(provider: String, idToken: String): Flow<Result<LoginResult>>

    /**
     * 온보딩 정보 조회
     * 
     * @return 온보딩 정보 (완료하지 않은 경우 실패)
     */
    suspend fun getOnboardingInfo(): Result<OnboardingInfo>

    /**
     * 온보딩 정보 제출
     * 
     * @param onboardingInfo 온보딩 정보
     * @return 온보딩 정보 제출 결과
     */
    suspend fun submitOnboarding(onboardingInfo: OnboardingInfo): Result<OnboardingInfo>

    /**
     * 저장된 액세스 토큰 존재 여부 확인
     * 
     * @return 토큰이 존재하면 true
     */
    suspend fun hasAccessToken(): Boolean
    
    /**
     * 현재 저장된 액세스 토큰 가져오기
     * 
     * @return 현재 액세스 토큰 (없으면 null)
     */
    suspend fun getCurrentAccessToken(): String?

    /**
     * 저장된 토큰 삭제
     */
    suspend fun clearTokens()
    
    /**
     * 토큰 갱신
     * 
     * @return 새로운 액세스 토큰, 리프레시 토큰
     */
    suspend fun refreshToken(): Result<LoginResult>
}