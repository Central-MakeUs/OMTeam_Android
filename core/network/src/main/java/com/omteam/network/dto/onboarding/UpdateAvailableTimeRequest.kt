package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 운동 가능 시간 수정 요청
 */
@Serializable
data class UpdateAvailableTimeRequest(
    val availableStartTime: String,
    val availableEndTime: String
)