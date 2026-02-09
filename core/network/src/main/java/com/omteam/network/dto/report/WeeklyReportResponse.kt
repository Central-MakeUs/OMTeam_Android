package com.omteam.network.dto.report

import kotlinx.serialization.Serializable

/**
 * 주간 리포트 응답 DTO
 */
@Serializable
data class WeeklyReportResponse(
    val success: Boolean,
    val data: WeeklyReportData? = null,
    val error: WeeklyReportError? = null
)

/**
 * 주간 리포트 데이터
 */
@Serializable
data class WeeklyReportData(
    val weekStartDate: String,
    val weekEndDate: String,
    val thisWeekSuccessRate: Double,
    val lastWeekSuccessRate: Double,
    val thisWeekSuccessCount: Int,
    val dailyResults: List<DailyResultDto>,
    val typeSuccessCounts: List<TypeSuccessCountDto>,
    val topFailureReasons: List<TopFailureReasonDto>,
    val aiFeedback: AiFeedbackDto
)

/**
 * 일일 결과 DTO
 */
@Serializable
data class DailyResultDto(
    val date: String,
    val dayOfWeek: String,
    val status: String,
    val missionType: String? = null,
    val missionTitle: String? = null
)

/**
 * 미션 타입별 성공 횟수 DTO
 */
@Serializable
data class TypeSuccessCountDto(
    val type: String,
    val typeName: String,
    val successCount: Int
)

/**
 * 상위 실패 사유 DTO
 */
@Serializable
data class TopFailureReasonDto(
    val rank: Int,
    val reason: String,
    val count: Int
)

/**
 * AI 피드백 DTO
 */
@Serializable
data class AiFeedbackDto(
    val failureReasonRanking: List<FailureReasonRankingDto>? = null,
    val weeklyFeedback: String? = null
)

/**
 * 실패 사유 순위 DTO
 */
@Serializable
data class FailureReasonRankingDto(
    val rank: Int,
    val category: String,
    val count: Int
)

/**
 * 주간 리포트 에러 정보
 */
@Serializable
data class WeeklyReportError(
    val code: String,
    val message: String
)