package com.omteam.domain.repository

import com.omteam.domain.model.LoginResult
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
}