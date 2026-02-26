package com.omteam.network.dto.notification

import kotlinx.serialization.Serializable

/**
 * FCM 토큰 API 응답
 */
@Serializable
data class FcmTokenResponse(
    val success: Boolean,
    val data: String? = null,
    val error: FcmTokenError? = null
)

@Serializable
data class FcmTokenError(
    val code: String,
    val message: String
)
