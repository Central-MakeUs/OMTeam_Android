package com.omteam.domain.model

/**
 * @property accessToken 액세스 토큰
 * @property refreshToken 리프레시 토큰
 * @property expiresIn 토큰 만료 시간 (초)
 * @property onboardingCompleted 온보딩 완료 여부
 */
data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val onboardingCompleted: Boolean
)