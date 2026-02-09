package com.omteam.network.dto.report

import kotlinx.serialization.Serializable

/**
 * 월간 요일별 패턴 분석 응답 DTO
 */
@Serializable
data class MonthlyPatternResponse(
    val success: Boolean,
    val data: MonthlyPatternData? = null,
    val error: MonthlyPatternError? = null
)

/**
 * 월간 패턴 데이터
 */
@Serializable
data class MonthlyPatternData(
    val startDate: String,
    val endDate: String,
    val dayOfWeekStats: List<DayOfWeekStatDto>,
    val aiFeedback: DayOfWeekAiFeedbackDto
)

/**
 * 요일별 통계 DTO
 */
@Serializable
data class DayOfWeekStatDto(
    val dayOfWeek: String,
    val dayName: String,
    val totalCount: Int,
    val successCount: Int,
    val failureCount: Int,
    val successRate: Double
)

/**
 * 요일별 AI 피드백 DTO
 */
@Serializable
data class DayOfWeekAiFeedbackDto(
    val dayOfWeekFeedbackTitle: String? = null,
    val dayOfWeekFeedbackContent: String? = null
)

/**
 * 월간 패턴 에러 정보
 */
@Serializable
data class MonthlyPatternError(
    val code: String,
    val message: String
)