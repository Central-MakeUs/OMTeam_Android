package com.omteam.data.datasource

import com.kakao.sdk.user.UserApiClient
import com.omteam.domain.model.auth.UserInfo
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 카카오 로그인 datasource 구현체
 *
 * 카카오 SDK 통해 유저 정보 조회, 로그아웃 처리
 */
class KakaoAuthDataSource @Inject constructor() : AuthDataSource {
    
    override suspend fun getUserInfo(): Result<UserInfo> = suspendCoroutine { continuation ->
        UserApiClient.instance.me { user, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                user != null -> {
                    val userInfo = UserInfo(
                        id = user.id ?: 0L,
                        nickname = user.kakaoAccount?.profile?.nickname,
                        email = user.kakaoAccount?.email
                    )
                    continuation.resume(Result.success(userInfo))
                }
                else -> continuation.resume(Result.failure(Exception("Unknown error")))
            }
        }
    }

    override suspend fun logout(): Result<Unit> = suspendCoroutine { continuation ->
        UserApiClient.instance.logout { error ->
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else {
                continuation.resume(Result.success(Unit))
            }
        }
    }
}