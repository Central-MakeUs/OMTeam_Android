package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 미션에 투자할 수 있는 시간 수정 요청
 */
@Serializable
data class UpdateMinExerciseMinutesRequest(
    val minExerciseMinutes: Int
)