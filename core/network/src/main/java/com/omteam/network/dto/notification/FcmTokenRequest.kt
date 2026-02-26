package com.omteam.network.dto.notification

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * FCM 토큰 등록/갱신 요청
 */
@Serializable
data class FcmTokenRequest(
    val fcmToken: String
)