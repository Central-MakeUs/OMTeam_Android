package com.omteam.network.interceptor

import com.omteam.domain.repository.AuthRepository
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 401, 403 응답 시 자동으로 토큰 갱신, 이전 요청 재시도
 *
 * Mutex로 중복 갱신 방지 + 최대 3회 재시도
 *
 * 토큰 갱신 API에 재시도 처리 추가하면 무한 루프 발생할 수 있어서 null 리턴시켜 로그아웃 처리
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val authRepository: Lazy<AuthRepository>
) : Authenticator {

    // 동시성 제어용 Mutex
    // 여러 요청이 동시에 401 에러를 받아도 토큰 갱신은 1회
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.d("## [TokenAuthenticator] 인증 실패 (${response.code}) - ${response.request.url}")

        // 토큰 갱신 API 자체가 실패한 경우 재시도하지 않음 (무한 루프 방지)
        if (response.request.url.encodedPath.contains("/auth/refresh")) {
            Timber.e("## [TokenAuthenticator] 토큰 갱신 API 실패 → 재시도 중단, 로그아웃 처리")
            // 토큰 삭제 후 null 반환 -> 로그아웃 처리
            runBlocking {
                authRepository.get().clearTokens()
            }

            return null
        }

        // 이미 재시도한 요청인지 확인 (3회 이상 재시도 방지)
        val retryCount = response.request.header("X-Retry-Count")?.toIntOrNull() ?: 0
        if (retryCount >= 3) {
            Timber.e("## [TokenAuthenticator] 재시도 횟수 초과 (${retryCount}회) → 중단")
            return null
        }

        // 현재 요청에 사용된 토큰
        val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")?.trim()

        // 토큰 갱신
        // OkHttp의 authenticate()는 동기 함수인데 호출해야 하는 함수들은 비동기 함수라 runBlocking으로 감싸서 사용
        return runBlocking {
            mutex.withLock {
                // 다른 요청에서 토큰을 갱신했는지 확인 (중복 갱신 방지)
                val currentToken = authRepository.get().getCurrentAccessToken()
                if (currentToken != null && currentToken != requestToken) {
                    // 이미 다른 요청이 토큰 갱신 → 저장된 새 토큰으로 재시도
                    Timber.d("## [TokenAuthenticator] 이미 갱신됨 → 새 토큰으로 재시도")
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .header("X-Retry-Count", (retryCount + 1).toString())
                        .build()
                }

                Timber.d("## [TokenAuthenticator] 토큰 갱신 시작")
                val result = authRepository.get().refreshToken()
                result.fold(
                    onSuccess = { loginResult ->
                        Timber.d("## [TokenAuthenticator] 토큰 갱신 성공 → 요청 재시도")

                        // 새 토큰으로 이전 요청 재시도
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${loginResult.accessToken}")
                            .header("X-Retry-Count", (retryCount + 1).toString())
                            .build()
                    },
                    onFailure = { error ->
                        Timber.e("## [TokenAuthenticator] 토큰 갱신 실패 - ${error.message}")

                        // 갱신 실패 시 토큰 삭제해서 로그아웃 처리
                        authRepository.get().clearTokens()
                        null
                    }
                )
            }
        }
    }
}