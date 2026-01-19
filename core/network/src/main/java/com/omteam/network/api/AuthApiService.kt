package com.omteam.network.api

import com.omteam.network.dto.LoginWithIdTokenRequest
import com.omteam.network.dto.LoginWithIdTokenResponse
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
}