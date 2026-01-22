package com.omteam.network.dto

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
    val preferredExerciseText: String,
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