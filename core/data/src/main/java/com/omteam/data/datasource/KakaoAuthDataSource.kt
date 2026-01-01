package com.omteam.data.datasource

import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoAuthDataSource @Inject constructor() {
    
    suspend fun getUserInfo(): Result<User> = suspendCoroutine { continuation ->
        UserApiClient.instance.me { user, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                user != null -> continuation.resume(Result.success(user))
                else -> continuation.resume(Result.failure(Exception("Unknown error")))
            }
        }
    }

    suspend fun logout(): Result<Unit> = suspendCoroutine { continuation ->
        UserApiClient.instance.logout { error ->
            if (error != null) {
                continuation.resume(Result.failure(error))
            } else {
                continuation.resume(Result.success(Unit))
            }
        }
    }
}
