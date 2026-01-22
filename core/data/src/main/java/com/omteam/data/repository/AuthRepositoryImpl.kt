package com.omteam.data.repository

import com.omteam.data.datasource.AuthDataSource
import com.omteam.data.di.GoogleAuth
import com.omteam.data.di.KakaoAuth
import com.omteam.data.mapper.toDomain
import com.omteam.datastore.TokenDataStore
import com.omteam.domain.model.LoginResult
import com.omteam.domain.model.OnboardingInfo
import com.omteam.domain.model.UserInfo
import com.omteam.domain.repository.AuthRepository
import com.omteam.network.api.AuthApiService
import com.omteam.network.dto.LoginWithIdTokenRequest
import com.omteam.network.dto.OnboardingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

/**
 * AuthDataSource 통해 구글, 카카오 로그인 지원
 */
class AuthRepositoryImpl @Inject constructor(
    @param:KakaoAuth private val kakaoAuthDataSource: AuthDataSource,
    @param:GoogleAuth private val googleAuthDataSource: AuthDataSource,
    private val authApiService: AuthApiService,
    private val tokenDataStore: TokenDataStore,
    private val json: Json
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
    
    override suspend fun getOnboardingInfo(): Result<OnboardingInfo> {
        return try {
            Timber.d("## 온보딩 정보 조회 시작")

            val response = authApiService.getOnboardingInfo()

            val data = response.data
            if (response.success && data != null) {
                Timber.d("## 온보딩 정보 조회 성공")
                Result.success(data.toDomain())
            } else {
                val errorMessage = response.error?.message ?: "온보딩 정보를 불러올 수 없습니다"
                val errorCode = response.error?.code
                Timber.e("## 온보딩 정보 조회 실패 - $errorCode: $errorMessage")
                // 에러 코드를 포함한 예외 생성
                Result.failure(Exception("$errorCode: $errorMessage"))
            }
        } catch (e: HttpException) {
            // 400, 403 등 에러 응답 본문 파싱
            // U003 에러는 400으로 리턴되는데 레트로핏이 이걸 예외로 던져서 응답 본문을 확인할 수 없어 추가함
            try {
                val errorBody = e.response()?.errorBody()?.string()
                // 응답 본문이 비어 있는 경우
                if (!errorBody.isNullOrBlank()) {
                    val errorResponse = json.decodeFromString<OnboardingResponse>(errorBody)
                    val errorCode = errorResponse.error?.code
                    val errorMessage = errorResponse.error?.message ?: "온보딩 정보 조회 실패"
                    Timber.e("## 온보딩 정보 조회 실패 (HTTP ${e.code()}) - $errorCode : $errorMessage")
                    Result.failure(Exception("$errorCode: $errorMessage"))
                } else {
                    // 응답 본문이 없는 경우
                    Timber.e(e, "## 온보딩 정보 조회 에러 (HTTP ${e.code()}) - 응답 본문 없음")
                    Result.failure(Exception("HTTP ${e.code()}: ${e.message()}"))
                }
            } catch (parseException: Exception) {
                Timber.e(parseException, "## 에러 응답 파싱 실패 (HTTP ${e.code()})")
                // 파싱 실패 시 HTTP 코드로 판단
                Result.failure(Exception("HTTP ${e.code()}: ${e.message()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "## 온보딩 정보 조회 예외 발생")
            Result.failure(e)
        }
    }
    
    override suspend fun hasAccessToken(): Boolean {
        val token = tokenDataStore.getAccessToken().firstOrNull()

        return !token.isNullOrEmpty()
    }
    
    override suspend fun clearTokens() {
        tokenDataStore.clearTokens()
        Timber.d("## 토큰 삭제 완료")
    }
}