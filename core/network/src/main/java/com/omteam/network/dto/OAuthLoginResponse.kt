package com.omteam.network.dto

import kotlinx.serialization.Serializable

/**
 * OAuth 로그인 응답 DTO
 *
 * @property success 성공 여부
 * @property data 로그인 성공 시 데이터
 * @property error 로그인 실패 시 에러 정보
 */
@Serializable
data class OAuthLoginResponse(
    val success: Boolean,
    val data: OAuthLoginData? = null,
    val error: OAuthLoginError? = null
)

/**
 * OAuth 로그인 성공 데이터
 *
 * @property accessToken 액세스 토큰
 * @property refreshToken 리프레시 토큰
 * @property expiresIn 토큰 만료 시간 (초)
 * @property onboardingCompleted 온보딩 완료 여부
 */
@Serializable
data class OAuthLoginData(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val onboardingCompleted: Boolean
)

/**
 * OAuth 로그인 에러 정보
 *
 * @property code 에러 코드
 * @property message 에러 메시지
 */
@Serializable
data class OAuthLoginError(
    val code: String,
    val message: String
)