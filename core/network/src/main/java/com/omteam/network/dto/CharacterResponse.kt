package com.omteam.network.dto

import com.omteam.network.dto.onboarding.ApiError
import kotlinx.serialization.Serializable

/**
 * 캐릭터 정보 조회 응답
 */
@Serializable
data class CharacterResponse(
    val success: Boolean,
    val data: CharacterData? = null,
    val error: ApiError? = null
)

@Serializable
data class CharacterData(
    val level: Int,
    val experiencePercent: Int,
    val successCount: Int,
    val successCountUntilNextLevel: Int,
    val encouragementTitle: String,
    val encouragementMessage: String
)