package com.omteam.main

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class KakaoLoginHelper(private val context: Context) {

    fun loginWithKakao(callback: (Boolean, String?) -> Unit) {
        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleLoginResult(token, error, callback)
            }
        } else {
            // 카카오 계정으로 로그인
            loginWithKakaoAccount(callback)
        }
    }

    private fun loginWithKakaoAccount(callback: (Boolean, String?) -> Unit) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            handleLoginResult(token, error, callback)
        }
    }

    private fun handleLoginResult(
        token: OAuthToken?,
        error: Throwable?,
        callback: (Boolean, String?) -> Unit
    ) {
        if (error != null) {
            callback(false, error.message)
        } else if (token != null) {
            // 로그인 성공
            getUserInfo(callback)
        }
    }

    private fun getUserInfo(callback: (Boolean, String?) -> Unit) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                callback(false, error.message)
            } else if (user != null) {
                val userId = user.id
                val nickname = user.kakaoAccount?.profile?.nickname
                val email = user.kakaoAccount?.email

                callback(true, "로그인 성공: $nickname")
            }
        }
    }
}