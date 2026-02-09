package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 닉네임 변경 요청
 */
@Serializable
data class UpdateNicknameRequest(
    val nickname: String
)