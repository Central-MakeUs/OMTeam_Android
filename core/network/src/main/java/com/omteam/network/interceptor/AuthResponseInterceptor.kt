package com.omteam.network.interceptor

import com.omteam.domain.repository.AuthRepository
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 401, 403 응답 시 자동으로 토큰 갱신 후 재시도
 *
 * OkHttp의 Authenticator는 401에만 작동하므로 Interceptor로 구현
 *
 * Mutex로 중복 갱신 방지 + 최대 3회 재시도
 */
@Singleton
class AuthResponseInterceptor @Inject constructor(
    private val authRepository: Lazy<AuthRepository>
) : Interceptor {

    // 동시성 제어용 Mutex
    // 여러 요청이 동시에 401/403 에러를 받아도 토큰 갱신은 1회만
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // 401 또는 403 응답이 아니면 그대로 반환
        if (response.code != 401 && response.code != 403) {
            return response
        }

        Timber.d("## [AuthResponseInterceptor] 인증 실패 (${response.code}) - ${request.url}")

        // 토큰 갱신 API 자체가 실패한 경우 재시도하지 않음 (무한 루프 방지)
        if (request.url.encodedPath.contains("/auth/refresh")) {
            Timber.e("## [AuthResponseInterceptor] 토큰 갱신 API 실패 → 재시도 중단, 로그아웃 처리")
            runBlocking {
                authRepository.get().clearTokens()
            }
            return response // 원본 응답 반환 (에러로 처리됨)
        }

        // 이미 재시도한 요청인지 확인 (3회 이상 재시도 방지)
        val retryCount = request.header("X-Retry-Count")?.toIntOrNull() ?: 0
        if (retryCount >= 3) {
            Timber.e("## [AuthResponseInterceptor] 재시도 횟수 초과 (${retryCount}회) → 중단")
            return response
        }

        // 응답을 닫아야 함 (재사용하지 않으므로)
        response.close()

        // 현재 요청에 사용된 토큰
        val requestToken = request.header("Authorization")?.removePrefix("Bearer ")?.trim()

        // 토큰 갱신 시도
        val newRequest = runBlocking {
            mutex.withLock {
                // 다른 요청에서 토큰을 갱신했는지 확인 (중복 갱신 방지)
                val currentToken = authRepository.get().getCurrentAccessToken()
                if (currentToken != null && currentToken != requestToken) {
                    // 이미 다른 요청이 토큰 갱신 → 저장된 새 토큰으로 재시도
                    Timber.d("## [AuthResponseInterceptor] 이미 갱신됨 → 새 토큰으로 재시도")
                    return@runBlocking buildRetryRequest(request, currentToken, retryCount)
                }

                Timber.d("## [AuthResponseInterceptor] 토큰 갱신 시작")
                val result = authRepository.get().refreshToken()
                result.fold(
                    onSuccess = { loginResult ->
                        Timber.d("## [AuthResponseInterceptor] 토큰 갱신 성공 → 요청 재시도")
                        buildRetryRequest(request, loginResult.accessToken, retryCount)
                    },
                    onFailure = { error ->
                        Timber.e("## [AuthResponseInterceptor] 토큰 갱신 실패 - ${error.message}")
                        // 갱신 실패 시 토큰 삭제해서 로그아웃 처리
                        authRepository.get().clearTokens()
                        null
                    }
                )
            }
        }

        // 토큰 갱신 실패 시 원본 에러 응답 반환
        if (newRequest == null) {
            Timber.e("## [AuthResponseInterceptor] 토큰 갱신 실패 → 원본 응답 반환")
            return chain.proceed(request) // 원본 요청을 다시 실행해서 에러 응답 받기
        }

        // 새 토큰으로 재시도
        Timber.d("## [AuthResponseInterceptor] 새 토큰으로 요청 재시도")
        return chain.proceed(newRequest)
    }

    /**
     * 재시도용 Request 생성
     */
    private fun buildRetryRequest(
        originalRequest: Request,
        newToken: String,
        retryCount: Int
    ): Request {
        return originalRequest.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .header("X-Retry-Count", (retryCount + 1).toString())
            .build()
    }
}