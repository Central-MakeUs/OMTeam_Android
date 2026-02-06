package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 선호 운동 수정 요청
 */
@Serializable
data class UpdatePreferredExerciseRequest(
    val preferredExercises: List<String>
)