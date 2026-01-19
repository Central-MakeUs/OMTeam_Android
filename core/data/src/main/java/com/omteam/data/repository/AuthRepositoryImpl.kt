package com.omteam.data.repository

import com.omteam.data.datasource.AuthDataSource
import com.omteam.data.mapper.toDomain
import com.omteam.domain.model.LoginResult
import com.omteam.domain.model.UserInfo
import com.omteam.domain.repository.AuthRepository
import com.omteam.network.api.AuthApiService
import com.omteam.network.dto.LoginWithIdTokenRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * AuthDataSource 통해 구글, 카카오 로그인 지원
 */
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun getUserInfo(): Result<UserInfo> = authDataSource.getUserInfo()

    override suspend fun logout(): Result<Unit> = authDataSource.logout()
    
    override fun loginWithIdToken(provider: String, idToken: String): Flow<Result<LoginResult>> = flow {
        try {
            Timber.d("## 서버 로그인 시작 - provider : $provider")
            
            val request = LoginWithIdTokenRequest(idToken = idToken)
            val response = authApiService.loginWithIdToken(provider, request)
            
            val data = response.data
            if (response.success && data != null) {
                Timber.d("## 서버 로그인 API 성공")
                emit(Result.success(data.toDomain()))
            } else {
                val errorMessage = response.error?.message ?: "알 수 없는 오류"
                Timber.e("## 서버 로그인 API 실패 - ${response.error?.code}: $errorMessage")
                emit(Result.failure(Exception(errorMessage)))
            }
        } catch (e: Exception) {
            Timber.e("## 서버 로그인 API 예외 발생 : $e")
            emit(Result.failure(e))
        }
    }
}