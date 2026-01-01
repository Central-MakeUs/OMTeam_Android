package com.omteam.main

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KakaoLoginManager {
    
    fun isKakaoTalkAvailable(context: Context): Boolean =
        UserApiClient.instance.isKakaoTalkLoginAvailable(context)
    
    suspend fun loginWithKakaoTalk(context: Context): Result<OAuthToken> = suspendCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                token != null -> continuation.resume(Result.success(token))
                else -> continuation.resume(Result.failure(Exception("Unknown error")))
            }
        }
    }
    
    suspend fun loginWithKakaoAccount(context: Context): Result<OAuthToken> = suspendCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                token != null -> continuation.resume(Result.success(token))
                else -> continuation.resume(Result.failure(Exception("Unknown error")))
            }
        }
    }
}