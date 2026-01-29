package com.omteam.network.dto.onboarding

import kotlinx.serialization.Serializable

/**
 * 온보딩 정보 제출 요청
 */
@Serializable
data class OnboardingRequest(
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
