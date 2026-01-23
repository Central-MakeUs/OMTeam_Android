package com.omteam.network.dto

import kotlinx.serialization.Serializable

/**
 * 토큰 갱신 요청
 * 
 * @property refreshToken 리프레시 토큰
 */
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)