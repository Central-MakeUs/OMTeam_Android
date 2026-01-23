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
    val checkInEnabled: Boolean,
    val reviewEnabled: Boolean
)

enum class WorkTimeType {
    FIXED,      // 고정 근무
    SHIFT       // 스케줄 근무 (교대 근무)
}

enum class LifestyleType {
    REGULAR_DAYTIME,        // 비교적 규칙적인 평일 주간 근무
    IRREGULAR_OVERTIME,     // 야근/불규칙한 일정이 잦음
    SHIFT_NIGHT,            // 교대 또는 밤샘 근무
    VARIABLE_DAILY          // 일정이 매일 달라 예측이 어려움
}