package com.omteam.network.interceptor

import com.omteam.datastore.TokenDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 토큰 인터셉터
 * 
 * API 요청 시 Authorization 헤더 자동 추가
 */
@Singleton
class TokenInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {
    
    // 토큰 캐싱 (메모리)
    @Volatile
    private var cachedToken: String? = null
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    init {
        // 앱 시작 시 토큰 캐시 초기화, 변경 감지
        // 토큰은 로그인, 로그아웃 시마다 변할 수 있어 지속적으로 관찰하기 위해 Flow를 collect
        scope.launch {
            tokenDataStore.getAccessToken().collect { token ->
                cachedToken = token
                Timber.d("## [TokenInterceptor] 토큰 캐시 업데이트")
            }
        }
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 로그인 API는 accessToken 필요 없음
        if (originalRequest.url.encodedPath.contains("/auth/oauth/")) {
            return chain.proceed(originalRequest)
        }

        // 캐시된 토큰 사용
        val accessToken = cachedToken

        // 토큰 없으면 원본 요청 그대로 진행
        if (accessToken.isNullOrEmpty()) {
            Timber.d("## [TokenInterceptor] accessToken이 없습니다")
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Timber.d("## [TokenInterceptor] Authorization 헤더 추가 - $accessToken")
        return chain.proceed(newRequest)
    }
}
