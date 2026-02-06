package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 온보딩 정보 조회 응답
 */
@Serializable
data class OnboardingResponse(
    val success: Boolean,
    val data: OnboardingData? = null,
    val error: ApiError? = null
)

@Serializable
data class OnboardingData(
    val nickname: String,
    val appGoalText: String,
    val workTimeType: String,
    val availableStartTime: String,
    val availableEndTime: String,
    val minExerciseMinutes: Int,
    val preferredExerciseText: String? = null, // 기존 필드 (호환성 유지)
    val preferredExercises: List<String>? = null, // 새 필드 (리스트)
    val lifestyleType: String,
    val remindEnabled: Boolean,
    val checkinEnabled: Boolean,
    val reviewEnabled: Boolean
)

@Serializable
data class ApiError(
    val code: String,
    val message: String
)