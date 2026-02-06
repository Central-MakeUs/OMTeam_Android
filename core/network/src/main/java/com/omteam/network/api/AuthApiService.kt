package com.omteam.network.api

import com.omteam.network.dto.auth.LoginWithIdTokenRequest
import com.omteam.network.dto.auth.LoginWithIdTokenResponse
import com.omteam.network.dto.auth.RefreshTokenRequest
import retrofit2.http.Body
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