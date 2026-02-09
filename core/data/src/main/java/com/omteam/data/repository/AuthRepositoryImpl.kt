package com.omteam.data.repository

import com.omteam.data.datasource.AuthDataSource
import com.omteam.data.di.GoogleAuth
import com.omteam.data.di.KakaoAuth
import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.datastore.TokenDataStore
import com.omteam.domain.model.auth.LoginResult
import com.omteam.domain.model.auth.UserInfo
import com.omteam.domain.repository.AuthRepository
import com.omteam.network.api.AuthApiService
import com.omteam.network.dto.auth.LoginWithIdTokenRequest
import com.omteam.network.dto.auth.RefreshTokenRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

/**
 * AuthDataSource 통해 구글, 카카오 로그인 지원
 */
class AuthRepositoryImpl @Inject constructor(
    @param:KakaoAuth private val kakaoAuthDataSource: AuthDataSource,
    @param:GoogleAuth private val googleAuthDataSource: AuthDataSource,
    private val authApiService: AuthApiService,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun getUserInfo(provider: String): Result<UserInfo> {
        val dataSource = when (provider.lowercase()) {
            "kakao" -> kakaoAuthDataSource
            "google" -> googleAuthDataSource
            else -> return Result.failure(Exception("지원하지 않는 provider입니다 : $provider"))
        }
        return dataSource.getUserInfo()
    }

    override suspend fun logout(provider: String): Result<Unit> {
        val dataSource = when (provider.lowercase()) {
            "kakao" -> kakaoAuthDataSource
            "google" -> googleAuthDataSource
            else -> return Result.failure(Exception("지원하지 않는 provider입니다 : $provider"))
        }
        return dataSource.logout()
    }
    
    override fun loginWithIdToken(provider: String, idToken: String): Flow<Result<LoginResult>> =
        safeApiCall(
            logTag = "$provider 로그인",
            apiCall = {
                val request = LoginWithIdTokenRequest(idToken = idToken)
                authApiService.loginWithIdToken(provider, request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
    
    override suspend fun hasAccessToken(): Boolean {
        val token = tokenDataStore.getAccessToken().firstOrNull()
        return !token.isNullOrEmpty()
    }
    
    override suspend fun getCurrentAccessToken(): String? =
        tokenDataStore.getAccessToken().firstOrNull()
    
    override suspend fun clearTokens() {
        tokenDataStore.clearTokens()
        Timber.d("## 토큰 삭제 완료")
    }
    
    override suspend fun refreshToken(): Result<LoginResult> {
        return try {
            Timber.d("## 토큰 갱신 시작")

            val refreshToken = tokenDataStore.getRefreshToken().firstOrNull()
            if (refreshToken.isNullOrEmpty()) {
                Timber.e("## refreshToken이 없습니다")
                return Result.failure(Exception("refreshToken이 없습니다"))
            }

            val request = RefreshTokenRequest(refreshToken)
            val response = authApiService.refreshToken(request)

            val data = response.data
            if (response.success && data != null) {
                Timber.d("## 토큰 갱신 성공")

                // 새 토큰들을 dataStore에 저장
                tokenDataStore.saveTokens(data.accessToken, data.refreshToken)
                Timber.d("## 새 토큰 저장 완료")

                Result.success(data.toDomain())
            } else {
                val errorMessage = response.error?.message ?: "토큰 갱신 실패"
                val errorCode = response.error?.code
                Timber.e("## 토큰 갱신 실패 - $errorCode: $errorMessage")
                Result.failure(Exception("$errorCode: $errorMessage"))
            }
        } catch (e: Exception) {
            Timber.e(e, "## 토큰 갱신 예외 발생")
            Result.failure(e)
        }
    }

    override suspend fun withdraw(): Result<String> {
        return try {
            Timber.d("## 회원탈퇴 시작")

            val response = authApiService.withdraw()

            if (response.success) {
                Timber.d("## 회원탈퇴 API 성공")

                // 카카오 연결 끊기
                kakaoAuthDataSource.withdraw()
                    .onSuccess { Timber.d("## 카카오 연결 끊기 성공") }
                    .onFailure { e -> Timber.e(e, "## 카카오 연결 끊기 실패 (무시하고 계속) : ${e.message}") }

                // 구글 연결 해제
                googleAuthDataSource.withdraw()
                    .onSuccess { Timber.d("## 구글 연결 해제 성공") }
                    .onFailure { e -> Timber.e(e, "## 구글 연결 해제 실패 (무시하고 계속) : ${e.message}") }

                // 4. 로컬 토큰 삭제
                tokenDataStore.clearTokens()
                Timber.d("## 토큰 삭제 완료")

                Result.success(response.data ?: "회원탈퇴가 완료되었습니다.")
            } else {
                val errorMessage = response.error?.message ?: "회원탈퇴 실패"
                val errorCode = response.error?.code
                Timber.e("## 회원탈퇴 실패 - $errorCode : $errorMessage")
                Result.failure(Exception("$errorCode : $errorMessage"))
            }
        } catch (e: Exception) {
            Timber.e(e, "## 회원탈퇴 예외 발생")
            Result.failure(e)
        }
    }
}