package com.omteam.domain.repository

import com.omteam.domain.model.auth.LoginResult
import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.model.auth.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUserInfo(provider: String): Result<UserInfo>
    suspend fun logout(provider: String): Result<Unit>
    
    /**
     * idToken으로 서버 로그인 (구글, 카카오)
     *
     * @param provider google, kakao
     * @param idToken 소셜 로그인 후 받은 idToken
     * @return 서버 로그인 결과 (accessToken, refreshToken 등) Flow
     */
    fun loginWithIdToken(provider: String, idToken: String): Flow<Result<LoginResult>>

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

    /**
     * 회원탈퇴
     *
     * @return 탈퇴 결과 메시지
     */
    suspend fun withdraw(): Result<String>
}