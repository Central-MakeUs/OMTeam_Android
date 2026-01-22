package com.omteam.domain.model

/**
 * 온보딩 정보
 */
data class OnboardingInfo(
    val nickname: String,
    val appGoalText: String,
    val workTimeType: WorkTimeType,
    val availableStartTime: String,
    val availableEndTime: String,
    val minExerciseMinutes: Int,
    val preferredExerciseText: String,
    val lifestyleType: LifestyleType,
    val remindEnabled: Boolean,
    val checkinEnabled: Boolean,
    val reviewEnabled: Boolean
)

enum class WorkTimeType {
    FIXED,
    FLEXIBLE
}

enum class LifestyleType {
    REGULAR_DAYTIME,
    REGULAR_NIGHTTIME,
    IRREGULAR
}